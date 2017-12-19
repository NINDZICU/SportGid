package com.kpfu.khlopunov.sportgid.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.activities.AuthentificationActivity;
import com.kpfu.khlopunov.sportgid.adapters.InteresAdapter;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hlopu on 24.10.2017.
 */

public class ProfileFragment extends Fragment implements ApiCallback {

    public final static int REQUEST_CODE_SIGN_IN = 110;
    private TextView toolbarTitle;
    private ImageButton btnAddEvent;
    private Toolbar toolbar;
    private ImageView ivPhoto;
    private TextView tvName;
    private RecyclerView rvInterests;
    private User user;
    private Button btnSignIn;
    private ConstraintLayout layout;
    private NotifyFragment notifyFragment;

    public static ProfileFragment getInstance() {

        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddEvent = view.findViewById(R.id.toolbar_btn_add_event);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        toolbar = view.findViewById(R.id.my_toolbar);
        ivPhoto = view.findViewById(R.id.iv_profile_image);
        tvName = view.findViewById(R.id.tv_profile_name);
        rvInterests = view.findViewById(R.id.rv_profile_interests);
        btnSignIn = view.findViewById(R.id.btn_profile_sign_in);
        layout = view.findViewById(R.id.constr_profile);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarTitle.setText("Профиль");
        btnAddEvent = toolbar.findViewById(R.id.toolbar_btn_add_event);

        if (SharedPreferencesProvider.getInstance(getActivity()).getUserTokken() != null) {
            btnAddEvent.setOnClickListener(v -> {
                AddEventDialogFragment dialogFragment = new AddEventDialogFragment();
                dialogFragment.show(getFragmentManager(), AddEventDialogFragment.class.getName());
            });

            user = SharedPreferencesProvider.getInstance(getActivity()).getUser();
            System.out.println("FROM SHARED PREFERENCES " + user);
            if (user == null) {
                ApiService apiService = new ApiService(getActivity());
                System.out.println("NURIK PIDR " + SharedPreferencesProvider.getInstance(getActivity()).getUserTokken());
                apiService.getUser(SharedPreferencesProvider.getInstance(getActivity()).getUserTokken(), ProfileFragment.this);
            } else {
                tvName.setText(user.getName() + " " + user.getSurname());
            }
            //TODO фото
            rvInterests.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            InteresAdapter adapter = new InteresAdapter(getActivity());

            List<KindSport> kindSports = new ArrayList<>();
            adapter.setKindSportList(kindSports);
            //TODO подгрузку интересов из пользователя
            rvInterests.setAdapter(adapter);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            btnAddEvent.setVisibility(View.GONE);
            btnSignIn.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AuthentificationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
            });
        }
    }

    @Override
    public void callback(Object object) {
        if (object != null) {
            user = (User) object;
            tvName.setText(user.getName() + " " + user.getSurname());
        } else {
            Toast.makeText(getActivity(), "Не удалось загрузить данные для пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    public void setNotifyFragment(NotifyFragment notifyFragment) {
        this.notifyFragment = notifyFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SIGN_IN:
                    notifyFragment.notifyData();

            }
        }
    }
}

