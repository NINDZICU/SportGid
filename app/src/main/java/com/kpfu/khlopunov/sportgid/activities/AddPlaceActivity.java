package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.kpfu.khlopunov.sportgid.service.PermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String KEY_URL = "URL";

    private List<Integer> kindsSport;

    private String photoUrlFirebase = null;
    private Uri selectedImage = null;

    private Pattern patternPhone = Pattern.compile(PHONE_PATTERN);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        bind();

        if (savedInstanceState != null) {
            String sUri = savedInstanceState.getString(KEY_URL);
            if (sUri != null) {
                selectedImage = Uri.parse(sUri);
                fillImage(selectedImage);
            }
        }
        kindsSport = new ArrayList<>();

        btnDownload.setOnClickListener(v -> {
            PermissionService permissionService = new PermissionService(AddPlaceActivity.this);
            if (permissionService.checkPermissionsStorage()) {
                runPhotoPicker();
            }
        });

        btnSave.setOnClickListener(v -> {
            uploadImage();
        });

        tvKindSport.setOnClickListener(v -> {
            Intent intent = new Intent(AddPlaceActivity.this, SelectInterestsActivtiy.class);
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
        uploadDataToServer();
        setVisibleProgressBar(false);
    }


    public boolean checkCorrectData() {
        if (!validatePhone(etNumber.getText().toString())) {
            Toast.makeText(this, "Введите корректный номер телефона", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etName.length() == 0 || etAddress.length() == 0 || etDescription.length() == 0 ||
                tvKindSport.length() == 0 || etPrice.length() == 0 || etPrice.length() == 0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        } else if (photoUrlFirebase == null) {
            Toast.makeText(this, "Не удалось загрузить фото на сервер", Toast.LENGTH_SHORT).show();
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
                    SharedPreferencesProvider.getInstance(AddPlaceActivity.this).getCity(), photoUrlFirebase
                    , kindsSport, AddPlaceActivity.this);
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
                    tvKindSport.setText(names.substring(0, names.length() - 2));
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
        if ((Boolean) object) {
            Toast.makeText(this, "Добавление успешно выполнено!", Toast.LENGTH_SHORT).show();
            finish();
        } else Toast.makeText(this, "Не удалось добавить объект", Toast.LENGTH_SHORT).show();
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
                    runPhotoPicker();
                } else {
                    Toast.makeText(AddPlaceActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
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
}
