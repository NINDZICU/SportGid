package com.kpfu.khlopunov.sportgid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.kpfu.khlopunov.sportgid.adapters.MyEventsAdapter;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hlopu on 24.10.2017.
 */

public class ProfileFragment extends Fragment implements ApiCallback, OnBackPressedListener {

    public final static int REQUEST_CODE_SIGN_IN = 110;
    private Context context;
    private TextView toolbarTitle;
    private ImageButton btnAddEvent;
    private Toolbar toolbar;
    private ImageView ivPhoto;
    private TextView tvName;
    private RecyclerView rvInterests;
    private RecyclerView rvMyEvents;
    private RecyclerView rvMyPlaces;
    private TextView tvMyEvent;
    private TextView tvMyPlaces;
    private User user;
    private Button btnSignIn;
    private ConstraintLayout layout;
    private NotifyFragment notifyFragment;
    private MyEventsAdapter eventsAdapter;
    private MyEventsAdapter placesAdapter;

    public static ProfileFragment getInstance(Context context) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setContext(context);
        return profileFragment;
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
        rvMyEvents = view.findViewById(R.id.rv_profile_my_events);
        rvMyPlaces = view.findViewById(R.id.rv_profile_places);
        tvMyEvent = view.findViewById(R.id.tv_my_events);
        tvMyPlaces = view.findViewById(R.id.tv_my_places);
        btnSignIn = view.findViewById(R.id.btn_profile_sign_in);
        layout = view.findViewById(R.id.constr_profile);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarTitle.setText("Профиль");
        btnAddEvent = toolbar.findViewById(R.id.toolbar_btn_add_event);

        if (SharedPreferencesProvider.getInstance(context).getUserTokken() != null) {
            btnAddEvent.setOnClickListener(v -> {
                AddEventDialogFragment dialogFragment = new AddEventDialogFragment();
                dialogFragment.show(getFragmentManager(), AddEventDialogFragment.class.getName());
                //TODO нужен колбэк после добавления обновлять списки моих мероприятий
            });

            user = SharedPreferencesProvider.getInstance(getActivity()).getUser();
            System.out.println("FROM SHARED PREFERENCES " + user);
            ApiService apiService = new ApiService(context);
            if (user == null) {
                System.out.println("NURIK PIDR " + SharedPreferencesProvider.getInstance(context).getUserTokken());
                apiService.getUser(SharedPreferencesProvider.getInstance(context).getUserTokken(), ProfileFragment.this);
            } else {
                tvName.setText((user.getName() + " " + user.getSurname()));
            }
            //TODO фото
            rvInterests.setLayoutManager(new GridLayoutManager(context, 5));
            InteresAdapter adapter = new InteresAdapter(context);

            List<KindSport> kindSports = new ArrayList<>();
            adapter.setKindSportList(kindSports);
            //TODO подгрузку интересов из пользователя
            rvInterests.setAdapter(adapter);

            rvMyEvents.setLayoutManager(new LinearLayoutManager(context));
            rvMyPlaces.setLayoutManager(new LinearLayoutManager(context));
            eventsAdapter = new MyEventsAdapter(context);
            placesAdapter = new MyEventsAdapter(context);
            rvMyEvents.setAdapter(eventsAdapter);
            rvMyPlaces.setAdapter(placesAdapter);

            eventsAdapter.setmEventListener(event -> {
                DetailEventFragment fragment = DetailEventFragment.newInstance(event, context);
                getChildFragmentManager().beginTransaction()
                        .add(R.id.frame_profile_fragment, fragment, DetailEventFragment.class.getName())
                        .addToBackStack(ProfileFragment.class.getName())
                        .commit();
            });
            placesAdapter.setPlaceListener(place -> {
                DetailPlaceFragment fragment = DetailPlaceFragment.newInstance(place);
                getChildFragmentManager().beginTransaction()
                        .add(R.id.frame_profile_fragment, fragment, DetailPlaceFragment.class.getName())
                        .addToBackStack(ProfileFragment.class.getName())
                        .commit();
            });
            //TODO как нибудь потом одним запросом сделать
            apiService.getMyEvents(SharedPreferencesProvider.getInstance(context).getUserTokken(), ProfileFragment.this);
            apiService.getMyPlaces(SharedPreferencesProvider.getInstance(context).getUserTokken(), ProfileFragment.this);


        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            btnAddEvent.setVisibility(View.GONE);
            btnSignIn.setOnClickListener(v -> {
                Intent intent = new Intent(context, AuthentificationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
            });
        }
    }

    @Override
    public void callback(Object object) {
        try {
            if (((List) object).size()!=0 && ((List) object) instanceof Event) {
                eventsAdapter.setEvents((List) object);
                eventsAdapter.notifyDataSetChanged();
                tvMyEvent.setVisibility(View.VISIBLE);

            }
            if (((List) object).size()!=0 &&((List) object).get(0) instanceof Place) {
                System.out.println("MY PLACE SUCCESSFULL");
                placesAdapter.setPlaces((List) object);
                placesAdapter.notifyDataSetChanged();
                tvMyPlaces.setVisibility(View.VISIBLE);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("NULL MY EVENT OR PLACE", "IndexOutOfBoundsException");
        }


        if (object instanceof User) {
            user = (User) object;
            tvName.setText(user.getName() + " " + user.getSurname());
        } else if (object == null) {
            Toast.makeText(context, "Не удалось загрузить данные для пользователя", Toast.LENGTH_SHORT).show();
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

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onBackPressed() {
        Log.d("BACK PRESSED", "PROFILE");
        getChildFragmentManager().popBackStack();
    }
}

