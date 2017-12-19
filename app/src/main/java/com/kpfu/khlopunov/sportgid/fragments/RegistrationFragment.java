package com.kpfu.khlopunov.sportgid.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hlopu on 24.10.2017.
 */

public class RegistrationFragment extends Fragment implements ApiCallback {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etRegName = view.findViewById(R.id.et_reg_name);
        etRegSurname = view.findViewById(R.id.et_reg_surname);
        etEmail = view.findViewById(R.id.et_reg_email);
        etPassword = view.findViewById(R.id.et_reg_password);
        etRepassword = view.findViewById(R.id.et_reg_repassword);
        btnRegistration = view.findViewById(R.id.btn_reg);
        tvErrPassword = view.findViewById(R.id.tv_error_reg_password);
        tvErrRepassword = view.findViewById(R.id.tv_error_reg_repassword);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCorrectData()) {
                    System.out.println("CITY "+SharedPreferencesProvider.getInstance(getActivity()).getCity());
                    ApiService apiService = new ApiService(getActivity());
                    apiService.registration(etRegName.getText().toString(), etRegSurname.getText().toString(),
                            etEmail.getText().toString(), etPassword.getText().toString(),
                            SharedPreferencesProvider.getInstance(getActivity()).getCity(), RegistrationFragment.this);
                }
            }
        });
    }

    public boolean checkCorrectData() {
        if (!validateEmail(etEmail.getText().toString())) {
            Toast.makeText(getActivity(), "Введите корректный email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etRegName.length() == 0 || etRegSurname.length() == 0 || etPassword.length() == 0 || etRepassword.length() == 0) {
            Toast.makeText(getActivity(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;

        } else if (etPassword.length() < 6) {
            Toast.makeText(getActivity(), "Длина пароля должна быть больше 6 символов!", Toast.LENGTH_SHORT).show();
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
        if (((String) object).equals("ERROR")) Toast.makeText(getActivity(),
                "Не удалось зарегистрироваться", Toast.LENGTH_SHORT).show();
        else if (((String) object).equals("SAME_LOGIN"))
            Toast.makeText(getActivity(), "Такой логин уже существует!!", Toast.LENGTH_SHORT).show();
        else {
            //TODO закрыть фрагмент
            SharedPreferencesProvider.getInstance(getActivity()).saveUserTokken((String) object);
            SharedPreferencesProvider.getInstance(getActivity()).saveUser(new User(
                    etRegName.getText().toString(), etRegSurname.getText().toString()));
            getActivity().getFragmentManager().popBackStack();
        }
    }
}
