package com.kpfu.khlopunov.sportgid.activities;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.kpfu.khlopunov.sportgid.service.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPlaceActivity extends AppCompatActivity implements ApiCallback, FireBaseCallback {
    static final int GALLERY_REQUEST = 1;
    public final static int REQUEST_CODE_INTERESTS = 101;
    public static final String PHONE_PATTERN = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    private ImageButton btnDownload;
    private EditText etName;
    private EditText etAddress;
    private EditText etDescription;
    private TextView tvKindSport;
    private EditText etPrice;
    private EditText etNumber;
    private Button btnSave;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private List<Integer> kindsSport;

    private String photoUrlFirebase = null;
    Uri selectedImage = null;

    private Pattern patternPhone = Pattern.compile(PHONE_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        btnDownload = findViewById(R.id.btn_place_download);
        etName = findViewById(R.id.et_place_name);
        etAddress = findViewById(R.id.et_place_address);
        etDescription = findViewById(R.id.et_place_description);
        tvKindSport = findViewById(R.id.tv_place_kind_sport);
        etPrice = findViewById(R.id.et_place_price);
        etNumber = findViewById(R.id.et_place_number);
        btnSave = findViewById(R.id.btn_place_save);
        toolbar = findViewById(R.id.my_toolbar_set);
        toolbarTitle = findViewById(R.id.toolbar_title_set);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarTitle.setText("Добавить объект");

        kindsSport = new ArrayList<>();

        btnDownload.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            uploadImage();
            if (checkCorrectData()) {
                ApiService apiService = new ApiService(this);
                apiService.addPlace(etAddress.getText().toString(), etNumber.getText().toString(), etName.getText().toString(),
                        etDescription.getText().toString(),
                        SharedPreferencesProvider.getInstance(AddPlaceActivity.this).getCity(), photoUrlFirebase
                        ,kindsSport, AddPlaceActivity.this);
            }
        });

        tvKindSport.setOnClickListener(v -> {
            Intent intent = new Intent(AddPlaceActivity.this, SelectInterestsActivtiy.class);
            startActivityForResult(intent, REQUEST_CODE_INTERESTS);
        });
    }

    public boolean checkCorrectData() {
        if (!validatePhone(etNumber.getText().toString())) {
            Toast.makeText(this, "Введите корректный номер телефона", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etName.length() == 0 || etAddress.length() == 0 || etDescription.length() == 0 ||
                tvKindSport.length() == 0 || etPrice.length() == 0 || etPrice.length() == 0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(photoUrlFirebase==null){
            Toast.makeText(this, "Не удалось загрузить фото на сервер", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validatePhone(String phone) {
        Matcher matcher = patternPhone.matcher(phone);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_INTERESTS:
                    Bundle bundle = data.getExtras();
                    List<KindSport> kindSports = (List<KindSport>) bundle.getSerializable("selected");
                    System.out.println("SIZE " + kindSports.size());
                    String names = "";
                    kindsSport = new ArrayList<>();
                    for (KindSport kindSport : kindSports) {
                        names += kindSport.getName() + ", ";
                        kindsSport.add(kindSport.getId());
                    }
                    tvKindSport.setText(names.substring(0, names.length() - 2));
                case GALLERY_REQUEST:
                    selectedImage = data.getData();
                    Glide.
                            with(this)
                            .load(selectedImage)
                            .apply(RequestOptions.fitCenterTransform())
                            .into(btnDownload);
            }
        }
    }

    @Override
    public void callback(Object object) {
        if ((Boolean) object) {
            Toast.makeText(this, "Добавление успешно выполнено!", Toast.LENGTH_SHORT).show();
            finish();
        } else Toast.makeText(this, "Не удалось добавить объект", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFirebaseLink(String firebaseLink) {
        this.photoUrlFirebase = firebaseLink;
    }

    private void uploadImage(){
        UploadImage uploadImage = UploadImage.getInstance();
        uploadImage.uploadImage(this, selectedImage);
    }
}
