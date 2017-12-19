package com.kpfu.khlopunov.sportgid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import com.google.android.gms.auth.api.Auth;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

/**
 * Created by hlopu on 24.10.2017.
 */

public class SettingsFragment extends Fragment implements ApiCallback {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView tvCity;
    private ImageButton ibEditCity;
    private ImageButton ibEditCityOk;
    private Button btnExit;
    private TextView tvCallback;
    private ViewSwitcher viewSwitcher;
    private ViewSwitcher viewSwitcherBtn;
    private EditText etCity;
    private NotifyFragment notifyFragment;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.my_toolbar_set);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title_set);
        tvCity = view.findViewById(R.id.tv_settings_city);
        ibEditCity = view.findViewById(R.id.btn_set_city);
        ibEditCityOk = view.findViewById(R.id.btn_set_ok_city);
        btnExit = view.findViewById(R.id.btn_settings_exit);
        tvCallback = view.findViewById(R.id.tv_settings_callback);
        viewSwitcher = view.findViewById(R.id.view_switcher);
        viewSwitcherBtn = view.findViewById(R.id.view_switcher_bnt_settings);
        etCity = view.findViewById(R.id.et_settings_city);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarTitle.setText("Настройки");

        tvCity.setText(SharedPreferencesProvider.getInstance(getActivity()).getCity());
        checkSignIn();

        ibEditCity.setOnClickListener(v -> {
            viewSwitcher.showNext();
            viewSwitcherBtn.showNext();
            etCity.setText(tvCity.getText());
        });
        ibEditCityOk.setOnClickListener(v -> {
            if (etCity.length() != 0) {
                ApiService apiService = new ApiService(getActivity());
                apiService.updateCity(SharedPreferencesProvider.getInstance(getActivity()).getUserTokken(),
                        etCity.getText().toString(), SettingsFragment.this);

            }

        });

        btnExit.setOnClickListener(v -> {
            btnExit.setVisibility(View.GONE);
            SharedPreferencesProvider.getInstance(getActivity()).deleteUserTokken();
            SharedPreferencesProvider.getInstance(getActivity()).deleteUser();
            VKAccessToken.removeTokenAtKey(getActivity(), VKAccessToken.ACCESS_TOKEN);
            VKSdk.logout();
//            Auth.GoogleSignInApi.signOut();//TODO реализовать
            notifyFragment.notifyData();
        });

        tvCallback.setOnClickListener(v -> {

        });
    }

    public void checkSignIn() {
        if (SharedPreferencesProvider.getInstance(getActivity()).getUserTokken() == null) {
            btnExit.setVisibility(View.GONE);
        } else {
            btnExit.setVisibility(View.VISIBLE);
        }
    }

    public void setNotifyFragment(NotifyFragment notifyFragment) {
        this.notifyFragment = notifyFragment;
    }

    @Override
    public void callback(Object object) {
        if ((Boolean) object) {
            tvCity.setText(etCity.getText());
            SharedPreferencesProvider.getInstance(getActivity()).saveCity(etCity.getText().toString());
            viewSwitcher.showNext();
            viewSwitcherBtn.showNext();
        } else
            Toast.makeText(getActivity(), "Не удалось изменить город!", Toast.LENGTH_SHORT).show();
    }
}
