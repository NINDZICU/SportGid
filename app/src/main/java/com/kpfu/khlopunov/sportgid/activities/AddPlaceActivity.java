package com.kpfu.khlopunov.sportgid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPlaceActivity extends AppCompatActivity {
    public static final String PHONE_PATTERN = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    private Button btnDownload;
    private EditText etName;
    private EditText etAddress;
    private EditText etDescription;
    private EditText etKindSport;
    private EditText etPrice;
    private EditText etNumber;
    private Button btnSave;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private Pattern patternPhone = Pattern.compile(PHONE_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        btnDownload = findViewById(R.id.btn_place_download);
        etName = findViewById(R.id.et_place_name);
        etAddress = findViewById(R.id.et_place_address);
        etDescription = findViewById(R.id.et_place_description);
        etKindSport = findViewById(R.id.et_place_kind_sport);
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

        btnSave.setOnClickListener(v -> {
            if(checkCorrectData()){
                List<Long> kindsSport = new ArrayList<>();
                kindsSport.add(1l);
                ApiService apiService = new ApiService(this);
                if(apiService.addPlace(etAddress.getText().toString(), etNumber.getText().toString(), etName.getText().toString(),
                        etDescription.getText().toString(), "Kazan", kindsSport)){
                    finish();
                }
            }
        });
    }

    public boolean checkCorrectData() {
        if (!validatePhone(etNumber.getText().toString())) {
            Toast.makeText(this, "Введите корректный номер телефона", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etName.length() == 0 || etAddress.length() == 0 || etDescription.length()==0 ||
                etKindSport.length() == 0 || etPrice.length() == 0 || etPrice.length() == 0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
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
}
