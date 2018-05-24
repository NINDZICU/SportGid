package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.databinding.ActivityAddEventBinding;
import com.kpfu.khlopunov.sportgid.firebase.FireBaseCallback;
import com.kpfu.khlopunov.sportgid.firebase.UploadImage;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.Event;
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

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.kpfu.khlopunov.sportgid.activities.AddPlaceActivity.KEY_URL;
import static com.kpfu.khlopunov.sportgid.activities.AddPlaceActivity.REQUEST_CODE_INTERESTS;
import static com.kpfu.khlopunov.sportgid.service.ServiceConstants.GALLERY_REQUEST;

public class AddEventActivity extends AppCompatActivity implements ApiCallback, FireBaseCallback {
    public static final String NOT_SELECTED_IMAGE = "NOT_SELECTED_IMAGE";
    private ActivityAddEventBinding r;

    private KindSport kindSport = null;
    private String photoUrlFirebase = null;
    private Uri selectedImage = null;
    private ActiveSystemServiceInt activeSystemService;
    private Event editEvent;
    private AddressResultReceiver mResultReceiver;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        r = DataBindingUtil.setContentView(this, R.layout.activity_add_event);
        bind();

        mResultReceiver = new AddressResultReceiver(null);
        activeSystemService = new ActiveSystemService(this);

        editEvent = (Event) getIntent().getSerializableExtra("EDIT_EVENT");
        if (editEvent != null) {
            fillFields(editEvent);
        }
        setVisibleProgressBar(false);
        if (savedInstanceState != null) {
            String sUri = savedInstanceState.getString(KEY_URL);
            if (sUri != null) {
                selectedImage = Uri.parse(sUri);
                fillImage(selectedImage);
            }
        }

        r.includeAddEvent.btnEventDownload.setOnClickListener(v -> {
            activeSystemService.runPhotoPicker();
        });
        r.includeAddEvent.btnEventSave.setOnClickListener(v -> {
            geocoding();
            setVisibleProgressBar(true);
        });
        r.includeAddEvent.tvEventKindSport.setOnClickListener(v -> {
            Intent intent = new Intent(AddEventActivity.this, SelectInterestsActivtiy.class);
            intent.putExtra(SelectInterestsActivtiy.TYPE_CHECK_BUTTON, SelectInterestsActivtiy.RADIO);
            startActivityForResult(intent, REQUEST_CODE_INTERESTS);
        });
    }

    private void bind() {
        setSupportActionBar(r.includeAddEvent.include.myToolbarSet);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        r.includeAddEvent.include.toolbarTitleSet.setText("Добавить мероприятие");
    }

    @Override
    public void setFirebaseLink(String firebaseLink) {
        this.photoUrlFirebase = firebaseLink;
        if (editEvent == null) uploadDataToServer();
        else updateEvent();
        setVisibleProgressBar(false);
    }


    public boolean checkCorrectData() {
        if (r.includeAddEvent.etEventName.length() == 0 || r.includeAddEvent.etEventAddress.length() == 0 ||
                r.includeAddEvent.etEventDescription.length() == 0 || r.includeAddEvent.tvEventKindSport.length() == 0 ||
                r.includeAddEvent.etEventPrice.length() == 0 || r.includeAddEvent.etEventCountMembers.length() == 0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        } else if (photoUrlFirebase == null) {
            Toast.makeText(this, "Не удалось загрузить фото на сервер", Toast.LENGTH_SHORT).show();
            return false;
        } else if (photoUrlFirebase.equals(NOT_SELECTED_IMAGE)) {
            photoUrlFirebase = null;
        }
        return true;
    }

    private void uploadDataToServer() {
        if (checkCorrectData()) {
            ApiService apiService = new ApiService(this);
            //todo возвращать координаты
            apiService.addEvent(r.includeAddEvent.etEventName.getText().toString(), r.includeAddEvent.etEventDescription.getText().toString(),
                    Integer.valueOf(r.includeAddEvent.etEventCountMembers.getText().toString()),
                    r.includeAddEvent.etEventPrice.getText().toString(), SharedPreferencesProvider.getInstance(this).getUserTokken(),
                    //Todo норм place и kindSport
                    photoUrlFirebase, String.valueOf(kindSport.getId()), null, latitude, longitude, AddEventActivity.this);

        }
    }

    private void updateEvent() {
        if (checkCorrectData()) {
            ApiService apiService = new ApiService(this);
            //todo возвращать координаты
            apiService.updateEvent(editEvent.getId(), r.includeAddEvent.etEventName.getText().toString(), r.includeAddEvent.etEventDescription.getText().toString(),
                    Integer.valueOf(r.includeAddEvent.etEventCountMembers.getText().toString()),
                    r.includeAddEvent.etEventPrice.getText().toString(), SharedPreferencesProvider.getInstance(this).getUserTokken(),
                    //Todo норм place и kindSport
                    photoUrlFirebase, String.valueOf(kindSport.getId()), null, latitude, longitude, AddEventActivity.this);

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
                    kindSport = kindSports.get(0);
                    r.includeAddEvent.tvEventKindSport.setText(kindSport.getName());
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
                .into(r.includeAddEvent.btnEventDownload);
    }

    @Override
    public void callback(Object object) {
        if (object instanceof Boolean) {
            if ((Boolean) object) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Добавление успешно выполнено!", Toast.LENGTH_SHORT).show();
                finish();
            } else Toast.makeText(this, "Не удалось добавить объект", Toast.LENGTH_SHORT).show();
        } else if (object instanceof Map) {
            latitude = ((Map) object).getX();
            longitude = ((Map) object).getY();
        } else if (object instanceof String) {
            if (object.equals("MAP_NON")) {
                Log.e("MAP", "Не удалось загрузить координаты");
                Toast.makeText(this, "Не удалось подгрузить данные с сервера", LENGTH_SHORT).show();
                setVisibleProgressBar(false);
            }
        }
    }

    private void uploadImage() {
        setVisibleProgressBar(true);
        UploadImage uploadImage = UploadImage.getInstance();
        uploadImage.uploadImageEvent(this, selectedImage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionService.REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activeSystemService.runPhotoPicker();
                } else {
                    Toast.makeText(AddEventActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    private void fillFields(Event event) {
        r.includeAddEvent.etEventName.setText(event.getName());
        r.includeAddEvent.etEventDescription.setText(event.getDescription());
        r.includeAddEvent.etEventCountMembers.setText(String.valueOf(event.getMaxOfMembers()));
        r.includeAddEvent.etEventAddress.setText(event.getAddress());
        r.includeAddEvent.etEventPrice.setText(event.getPrice());
        photoUrlFirebase = event.getPhoto();
        fillImage(Uri.parse(event.getPhoto()));

        setVisibleProgressBar(true);
        ApiService apiService = new ApiService(this);
        apiService.getMap(event.getMap(), this);
        //TODO getMap обрабатывать лучше

        for (KindSport kindSportLocal : SharedPreferencesProvider.getInstance(AddEventActivity.this).getKindsSports()) {
            if (kindSportLocal.getId() == event.getSport()) {
                kindSport = kindSportLocal;
                r.includeAddEvent.tvEventKindSport.setText(kindSport.getName());
                break;
            }
        }
    }


    private void setVisibleProgressBar(boolean bool) {
        if (bool) {
            r.includeAddEvent.pbAddEvent.setVisibility(View.VISIBLE);
            r.includeAddEvent.scrollviewAddEvent.setVisibility(View.GONE);
        } else {
            r.includeAddEvent.pbAddEvent.setVisibility(View.GONE);
            r.includeAddEvent.scrollviewAddEvent.setVisibility(View.VISIBLE);

        }
    }

    private void geocoding() {
        Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        intent.putExtra(ServiceConstants.RECEIVER, mResultReceiver);
        intent.putExtra(ServiceConstants.FETCH_TYPE_EXTRA, ServiceConstants.USE_ADDRESS_NAME);
        if (r.includeAddEvent.etEventAddress.getText().length() == 0) {
            Toast.makeText(AddEventActivity.this, "Введите Адрес", LENGTH_SHORT).show();
            setVisibleProgressBar(false);
            return;
        }
        intent.putExtra(ServiceConstants.LOCATION_NAME_DATA_EXTRA, r.includeAddEvent.etEventAddress.getText().toString());
        Log.e("ADDEVENTACTIVITY", "Starting Service");
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
                runOnUiThread(() -> {
                    System.out.println("Latitude: " + address.getLatitude() + "\n" +
                            "Longitude: " + address.getLongitude() + "\n" +
                            "Address: " + resultData.getString(ServiceConstants.RESULT_DATA_KEY));
                    longitude = address.getLongitude();
                    latitude = address.getLatitude();
                    if (photoUrlFirebase == null && selectedImage == null) {
                        Toast.makeText(AddEventActivity.this, "Выберите фото", Toast.LENGTH_SHORT).show();
                    } else if (editEvent != null && editEvent.getPhoto().equals(photoUrlFirebase))
                        setFirebaseLink(photoUrlFirebase);
                    else uploadImage();

                });
            } else {
                runOnUiThread(() -> {
                    System.out.println("receiveERROR " + resultData.getString(ServiceConstants.RESULT_DATA_KEY));
                    Toast.makeText(AddEventActivity.this, "Адрес не существует", LENGTH_SHORT).show();
                    setVisibleProgressBar(false);
                });

            }
        }
    }
}
