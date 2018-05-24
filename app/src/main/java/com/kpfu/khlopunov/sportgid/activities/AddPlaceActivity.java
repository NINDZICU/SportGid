package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.firebase.FireBaseCallback;
import com.kpfu.khlopunov.sportgid.firebase.UploadImage;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Map;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ActiveSystemService;
import com.kpfu.khlopunov.sportgid.service.ActiveSystemServiceInt;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.kpfu.khlopunov.sportgid.service.GeocodeAddressIntentService;
import com.kpfu.khlopunov.sportgid.service.PermissionService;
import com.kpfu.khlopunov.sportgid.service.ServiceConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_SHORT;
import static com.kpfu.khlopunov.sportgid.service.ServiceConstants.GALLERY_REQUEST;

public class AddPlaceActivity extends AppCompatActivity implements ApiCallback, FireBaseCallback {
    public final static int REQUEST_CODE_INTERESTS = 101;
    public static final String PHONE_PATTERN = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    private ImageView btnDownload;
    private EditText etName;
    private EditText etAddress;
    private EditText etDescription;
    private TextView tvKindSport;
    private EditText etPrice;
    private EditText etNumber;
    private Button btnSave;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ProgressBar progressBar;
    private ScrollView scrollView;


    public static final String KEY_URL = "KEY_URL";

    private List<Integer> kindsSport;
    private String photoUrlFirebase = null;
    private Uri selectedImage = null;
    private ActiveSystemServiceInt activeSystemService;
    private AddressResultReceiver mResultReceiver;

    private Pattern patternPhone = Pattern.compile(PHONE_PATTERN);
    private Place editPlace;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        bind();

        mResultReceiver = new AddressResultReceiver(null);
        editPlace = (Place) getIntent().getSerializableExtra("EDIT_PLACE");
        if (editPlace != null) {
            fillFields(editPlace);
        }
        setVisibleProgressBar(false);

        if (savedInstanceState != null) {
            String sUri = savedInstanceState.getString(KEY_URL);
            if (sUri != null) {
                selectedImage = Uri.parse(sUri);
                fillImage(selectedImage);
            }
        }
        kindsSport = new ArrayList<>();
        activeSystemService = new ActiveSystemService(this);

        btnDownload.setOnClickListener(v -> {
            activeSystemService.runPhotoPicker();
        });

        btnSave.setOnClickListener(v -> {
            geocoding();
            setVisibleProgressBar(true);
//            if (photoUrlFirebase == null && selectedImage == null) {
//                Toast.makeText(AddPlaceActivity.this, "Выберите фото", Toast.LENGTH_SHORT).show();
//            } else if (editPlace != null && editPlace.getPhoto().equals(photoUrlFirebase))
//                setFirebaseLink(photoUrlFirebase);
//            else uploadImage();
        });

        tvKindSport.setOnClickListener(v -> {
            Intent intent = new Intent(AddPlaceActivity.this, SelectInterestsActivtiy.class);
            intent.putExtra(SelectInterestsActivtiy.TYPE_CHECK_BUTTON, SelectInterestsActivtiy.CHECKBOX);
            startActivityForResult(intent, REQUEST_CODE_INTERESTS);
        });
    }

    private void bind() {
        btnDownload = findViewById(R.id.btn_place_download);
        etName = findViewById(R.id.et_place_name);
        etAddress = findViewById(R.id.et_place_address);
        etDescription = findViewById(R.id.et_place_description);
        tvKindSport = findViewById(R.id.tv_place_kind_sport);
        etPrice = findViewById(R.id.et_place_price);
        etNumber = findViewById(R.id.et_place_number);
        btnSave = findViewById(R.id.btn_place_save);
        progressBar = findViewById(R.id.pb_add_place);
        scrollView = findViewById(R.id.scrollview_add_place);
        toolbar = findViewById(R.id.my_toolbar_set);
        toolbarTitle = findViewById(R.id.toolbar_title_set);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarTitle.setText("Добавить объект");
    }

    @Override
    public void setFirebaseLink(String firebaseLink) {
        this.photoUrlFirebase = firebaseLink;
        if (editPlace != null) updateDataOnServer();
        else uploadDataToServer();

        setVisibleProgressBar(false);
    }


    public boolean checkCorrectData() {
        if (!validatePhone(etNumber.getText().toString())) {
            Toast.makeText(this, "Введите корректный номер телефона", LENGTH_SHORT).show();
            return false;
        } else if (etName.length() == 0 || etAddress.length() == 0 || etDescription.length() == 0 ||
                tvKindSport.length() == 0 || etPrice.length() == 0 || etPrice.length() == 0) {
            Toast.makeText(this, "Заполните все поля", LENGTH_SHORT).show();
            return false;
        } else if (photoUrlFirebase == null) {
            Toast.makeText(this, "Не удалось загрузить фото на сервер", LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validatePhone(String phone) {
        Matcher matcher = patternPhone.matcher(phone);
        return matcher.matches();
    }

    private void uploadDataToServer() {
        if (checkCorrectData()) {
            ApiService apiService = new ApiService(this);
            apiService.addPlace(etAddress.getText().toString(), etNumber.getText().toString(), etName.getText().toString(),
                    etDescription.getText().toString(),
                    SharedPreferencesProvider.getInstance(AddPlaceActivity.this).getCity(), photoUrlFirebase,
                    kindsSport, SharedPreferencesProvider.getInstance(this).getUserTokken(), latitude, longitude, AddPlaceActivity.this);
        }
    }

    private void updateDataOnServer() {
        if (checkCorrectData()) {
            ApiService apiService = new ApiService(this);
            apiService.updatePlace(editPlace.getId(), etAddress.getText().toString(), etNumber.getText().toString(), etName.getText().toString(),
                    etDescription.getText().toString(),
                    SharedPreferencesProvider.getInstance(AddPlaceActivity.this).getCity(), photoUrlFirebase
                    , kindsSport, SharedPreferencesProvider.getInstance(this).getUserTokken(), AddPlaceActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedImage != null) {
            outState.putString(KEY_URL, selectedImage.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_INTERESTS:
                    Bundle bundle = data.getExtras();
                    List<KindSport> kindSports = (List<KindSport>) bundle.getSerializable("selected");
                    String names = "";
                    kindsSport = new ArrayList<>();
                    for (KindSport kindSport : kindSports) {
                        names += kindSport.getName() + ", ";
                        kindsSport.add(kindSport.getId());
                    }
                    if (names.length() != 0)
                        tvKindSport.setText(names.substring(0, names.length() - 2));
                    else tvKindSport.setText("");
                    break;
                case GALLERY_REQUEST:
                    selectedImage = data.getData();
                    fillImage(selectedImage);
                    break;
            }
            System.out.println("SELECTED IMAGE@@@@ " + selectedImage);
//            if (selectedImage != null) {
//                fillImage(selectedImage);
//            }
        }
    }

    private void fillImage(Uri selectedImage) {
        Glide.
                with(this)
                .load(selectedImage)
                .apply(RequestOptions.fitCenterTransform().apply(RequestOptions.overrideOf(500)))
                .into(btnDownload);
    }

    @Override
    public void callback(Object object) {
        if(object instanceof Boolean) {
            if ((Boolean) object) {
                Toast.makeText(this, "Добавление успешно выполнено!", LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else Toast.makeText(this, "Не удалось добавить объект", LENGTH_SHORT).show();
        }
        else if(object instanceof Map){
            latitude = ((Map) object).getX();
            longitude = ((Map) object).getY();
        }
        else if(object instanceof String){
            if(object.equals("MAP_NON")){
                Log.e("MAP", "Не удалось загрузить координаты");
                Toast.makeText(this, "Не удалось подгрузить данные с сервера", LENGTH_SHORT).show();
                setVisibleProgressBar(false);
            }
        }
    }

    private void uploadImage() {
        UploadImage uploadImage = UploadImage.getInstance();
        uploadImage.uploadImage(this, selectedImage);
        setVisibleProgressBar(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionService.REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activeSystemService.runPhotoPicker();
                } else {
                    Toast.makeText(AddPlaceActivity.this, "GET_ACCOUNTS Denied",
                            LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    private void setVisibleProgressBar(boolean bool) {
        if (bool) {
            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

        }
    }

    private void fillFields(Place place) {
        etName.setText(place.getTitle());
        etPrice.setText("0");
        etDescription.setText(place.getDescription());
        etAddress.setText(place.getAddress());
        etNumber.setText(place.getContact());
        photoUrlFirebase = place.getPhoto();
        kindsSport = place.getSport();

        setVisibleProgressBar(true);
        ApiService apiService = new ApiService(this);
        apiService.getMap(place.getMap(), this);
        //TODO getMap обрабатывать лучше
        fillImage(Uri.parse(place.getPhoto()));

        String names = "";
        for (Integer idKindSport : kindsSport) {
            for (KindSport kindSportLocal : SharedPreferencesProvider.getInstance(AddPlaceActivity.this).getKindsSports()) {
                if (idKindSport.equals(kindSportLocal.getId())) {
                    names += kindSportLocal.getName() + ", ";
                }
            }
        }
        if (names.length() != 0) tvKindSport.setText(names.substring(0, names.length() - 2));
        else tvKindSport.setText("");
    }

    //    private void geocoding(String address){
//        System.out.println("START GEOCODING");
//        GoogleRequests googleRequests = GoogleApi.getInstance().getmGoogleRequests();
//        googleRequests.geocodeByAddress(address, API_KEY_GOOGLE).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(geocoding -> {
//                    Log.d("STATUS", geocoding.getStatus());
//                    Log.d("Error Message", geocoding.getError_message());
//                   if(geocoding.getStatus().equals("OK")){
//                       Log.d("LONGITUDE", geocoding.getResults().get(0).getGeometry().getLocation().getLat().toString()
//                       +"   "+geocoding.getResults().get(0).getGeometry().getLocation().getLng());
//                   }
//                }, throwable -> {
//                    Log.d("THROW geocoding", throwable.getMessage());
//                    Toast.makeText(AddPlaceActivity.this, "geocoding" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
    private void geocoding() {
        //TODO добавить проверку на то, изменился ли адрес когда изменяют плэйс, и у евента тоже
        Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        intent.putExtra(ServiceConstants.RECEIVER, mResultReceiver);
        intent.putExtra(ServiceConstants.FETCH_TYPE_EXTRA, ServiceConstants.USE_ADDRESS_NAME);
        if (etAddress.getText().length() == 0) {
            Toast.makeText(AddPlaceActivity.this, "Введите Адрес", LENGTH_SHORT).show();
            setVisibleProgressBar(false);
            return;
        }
        intent.putExtra(ServiceConstants.LOCATION_NAME_DATA_EXTRA, etAddress.getText().toString());
        Log.e("ADDPLACEACTIVITY", "Starting Service");
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == ServiceConstants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(ServiceConstants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(ServiceConstants.RESULT_DATA_KEY));
                        longitude = address.getLongitude();
                        latitude = address.getLatitude();
                        if (photoUrlFirebase == null && selectedImage == null) {
                            Toast.makeText(AddPlaceActivity.this, "Выберите фото", Toast.LENGTH_SHORT).show();
                        } else if (editPlace != null && editPlace.getPhoto().equals(photoUrlFirebase))
                            setFirebaseLink(photoUrlFirebase);
                        else uploadImage();
                    }
                });
            } else {
                runOnUiThread(() -> {
                    System.out.println("receiveERROR " + resultData.getString(ServiceConstants.RESULT_DATA_KEY));
                    Toast.makeText(AddPlaceActivity.this, "Адрес не существует", LENGTH_SHORT).show();
                    setVisibleProgressBar(false);
                });

            }
        }
    }
}
