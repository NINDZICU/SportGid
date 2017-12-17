package com.kpfu.khlopunov.sportgid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.kpfu.khlopunov.sportgid.Constants;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.EventAdapter;
import com.kpfu.khlopunov.sportgid.custom.NoDefaultSpinner;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 14.12.2017.
 */

public class ListEventsFragment extends Fragment {
    private Button btnObjects;
    private Button btnEvents;
    private NoDefaultSpinner spinnerSort;
    private ImageButton btnFilter;
    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private EventsListener eventsListener;

    public static ListEventsFragment newInstance(){
        return new ListEventsFragment();
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

        btnObjects.setOnClickListener(v -> {
            if(eventsListener!= null){
                ListObjectsFragment fragment = ListObjectsFragment.newInstance();
                fragment.setEventsListener(eventsListener);
                eventsListener.onButtonClicked(fragment);
            }
//            getChildFragmentManager().beginTransaction()
//                    .add(R.id.frame_home_fragment, fragment, ListObjectsFragment.class.getName())
//                    .addToBackStack(HomeFragment.class.getName())
//                    .commit();
        });

        eventAdapter.setmEventListener(event -> {
            DetailEventFragment fragment = DetailEventFragment.newInstance(event);
            eventsListener.onButtonClicked(fragment);
        });
        List<Event> events = new ArrayList<>();
        events.add(new Event(1, "EVENT", "EVENT", new Place(1,"sad",2,"s","s","s",new User("Sdad","s","s","s"),new KindSport(1,"sdad","asddasd"),null, "adsasd"),
                "sadasd", "123", 2,new User("Sdad","s","s","s"), "dsadas", null ,new KindSport(1,"sdad","asddasd")));
        events.add(new Event(1, "EVENT", "EVENT", new Place(1,"sad",2,"s","s","s",new User("Sdad","s","s","s"),new KindSport(1,"sdad","asddasd"),null, "adsasd"),
                "sadasd", "123", 2,new User("Sdad","s","s","s"), "dsadas", null ,new KindSport(1,"sdad","asddasd")));
        eventAdapter.setmEventList(events);
        rvEvents.setAdapter(eventAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Constants.DATA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setPrompt("Сортировать по...");
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
//    public interface ObjectsListener {
//        void onButtonClick(Fragment nextFragment);
//    }
//
//    public void setObjectsListener(ObjectsListener objectsListener) {
//        this.objectsListener = objectsListener;
//    }

    public void setEventsListener(EventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }
}
