package com.kpfu.khlopunov.sportgid.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.InteresAdapter;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 24.10.2017.
 */

public class ProfileFragment extends Fragment {

    private TextView toolbarTitle;
    private ImageButton btnAddEvent;
    private Toolbar toolbar;
    private ImageView ivPhoto;
    private TextView tvName;
    private RecyclerView rvInterests;

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

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarTitle.setText("Профиль");
        btnAddEvent = toolbar.findViewById(R.id.toolbar_btn_add_event);
        btnAddEvent.setOnClickListener(v -> {
            AddEventDialogFragment dialogFragment = new AddEventDialogFragment();
            dialogFragment.show(getFragmentManager(), AddEventDialogFragment.class.getName());
        });

        User user = new User("sdada", "asdasd","sadasd", "asdasd");
        //TODO фото
        tvName.setText(user.getName());
        rvInterests.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        InteresAdapter adapter = new InteresAdapter(getActivity());

        List<KindSport> kindSports = new ArrayList<>();
        adapter.setKindSportList(kindSports);
        //TODO подгрузку интересов из пользователя
        rvInterests.setAdapter(adapter);
    }
}
