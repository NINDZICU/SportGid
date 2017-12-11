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

import com.kpfu.khlopunov.sportgid.R;

/**
 * Created by hlopu on 24.10.2017.
 */

public class RegistrationFragment extends Fragment {

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
                if(!etPassword.getText().toString().equals(etRepassword.getText().toString())){
                    etPassword.setText("");
                    etRepassword.setText("");
                    tvErrRepassword.setText("Неверный пароль");
                }
            }
        });
    }


}
