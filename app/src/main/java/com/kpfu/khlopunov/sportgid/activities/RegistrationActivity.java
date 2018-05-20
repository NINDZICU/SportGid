package com.kpfu.khlopunov.sportgid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hlopu on 20.05.2018.
 */
//TODO удалить RegistrationFragment
public class RegistrationActivity extends AppCompatActivity implements ApiCallback {
    private EditText etRegName;
    private EditText etRegSurname;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepassword;
    private Button btnRegistration;
    private TextView tvErrRegName;
    private TextView tvErrRegSurname;
    private TextView tvErrEmail;
    private TextView tvErrPassword;
    private TextView tvErrRepassword;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        etRegName = findViewById(R.id.et_reg_name);
        etRegSurname = findViewById(R.id.et_reg_surname);
        etEmail = findViewById(R.id.et_reg_email);
        etPassword = findViewById(R.id.et_reg_password);
        etRepassword = findViewById(R.id.et_reg_repassword);
        btnRegistration = findViewById(R.id.btn_reg);
        tvErrPassword = findViewById(R.id.tv_error_reg_password);
        tvErrRepassword = findViewById(R.id.tv_error_reg_repassword);

        btnRegistration.setOnClickListener(v -> {
            if (checkCorrectData()) {
                System.out.println("CITY " + SharedPreferencesProvider.getInstance(this).getCity());
                ApiService apiService = new ApiService(this);
                apiService.registration(etRegName.getText().toString(), etRegSurname.getText().toString(),
                        etEmail.getText().toString(), etPassword.getText().toString(),
                        SharedPreferencesProvider.getInstance(this).getCity(), RegistrationActivity.this);
            }
        });
    }

    public boolean checkCorrectData() {
        if (!validateEmail(etEmail.getText().toString())) {
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etRegName.length() == 0 || etRegSurname.length() == 0 || etPassword.length() == 0 || etRepassword.length() == 0) {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;

        } else if (etPassword.length() < 6) {
            Toast.makeText(this, "Длина пароля должна быть больше 6 символов!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!etPassword.getText().toString().equals(etRepassword.getText().toString())) {
            etPassword.setText("");
            etRepassword.setText("");
            tvErrRepassword.setText("Неверный пароль");
            return false;
        } else return true;
    }

    public boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void callback(Object object) {
        System.out.println("TOKKKKEKEN OT NURIKA " + (String) object);
        if (((String) object).equals("ERROR")) Toast.makeText(this,
                "Не удалось зарегистрироваться", Toast.LENGTH_SHORT).show();
        else if (((String) object).equals("SAME_LOGIN"))
            Toast.makeText(this, "Такой логин уже существует!!", Toast.LENGTH_SHORT).show();
        else {
            //TODO закрыть фрагмент
            SharedPreferencesProvider.getInstance(this).saveUserTokken((String) object);
            SharedPreferencesProvider.getInstance(this).saveUser(new User(
                    etRegName.getText().toString(), etRegSurname.getText().toString()));
            finish();
        }
    }
}
