package com.kpfu.khlopunov.sportgid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.activities.AuthentificationActivity;
import com.kpfu.khlopunov.sportgid.adapters.EventAdapter;
import com.kpfu.khlopunov.sportgid.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 13.12.2017.
 */

public class ListObjectsFragment extends Fragment {
    private Button btnObjects;
    private Button btnEvents;
    private Spinner spinnerSort;
    private ImageButton btnFilter;
    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;

    public static ListObjectsFragment newInstance(){
        return new ListObjectsFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_events_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnObjects = view.findViewById(R.id.btn_objects);
        btnEvents = view.findViewById(R.id.btn_events);
        btnFilter = view.findViewById(R.id.button_filter);
        spinnerSort = view.findViewById(R.id.spinner);
        rvEvents = view.findViewById(R.id.rv_events);
        System.out.println("SDJAKSHJSAKH");
        rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventAdapter = new EventAdapter(getActivity());
        eventAdapter.setmEventListener(event -> {
            //TODO

        });
        List<Event> events = new ArrayList<>();
        events.add(new Event("SDDS", "SDSD", "sadasd", "123", "4.0"));
        events.add(new Event("SDDS", "SDSD", "sadasd", "123", "4.0"));
        events.add(new Event("SDDS", "SDSD", "sadasd", "123", "4.0"));
        eventAdapter.setmEventList(events);
        rvEvents.setAdapter(eventAdapter);


    }
}
