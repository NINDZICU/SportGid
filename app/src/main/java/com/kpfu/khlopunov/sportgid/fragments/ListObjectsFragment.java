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
import android.widget.ProgressBar;

import com.kpfu.khlopunov.sportgid.Constants;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.PlaceAdapter;
import com.kpfu.khlopunov.sportgid.custom.NoDefaultSpinner;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.kpfu.khlopunov.sportgid.service.SortService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hlopu on 13.12.2017.
 */

public class ListObjectsFragment extends Fragment implements NotifyFragment {
    private Button btnObjects;
    private Button btnEvents;
    private NoDefaultSpinner spinnerSort;
    private ImageButton btnFilter;
    private RecyclerView rvEvents;
    private PlaceAdapter placeAdapter;
    private EventsListener eventsListener;
    private ProgressBar progressBar;

    public static ListObjectsFragment newInstance(int idKind) {
        Bundle bundle = new Bundle();
        bundle.putInt("idKind", idKind);
        ListObjectsFragment fragment = new ListObjectsFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        progressBar = view.findViewById(R.id.pb_events);
        setVisible();
        rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        placeAdapter = new PlaceAdapter(getActivity(), ListObjectsFragment.this);
        placeAdapter.setmPlaceListener(place -> {
            DetailPlaceFragment fragment = DetailPlaceFragment.newInstance(place);
            eventsListener.onButtonClicked(fragment);

        });

        btnEvents.setOnClickListener(v -> {
            if (eventsListener != null) {
                System.out.println("NURIKU IDKIND " + getArguments().getInt("idKind"));
                ListEventsFragment fragment = ListEventsFragment.newInstance(getArguments().getInt("idKind"));
                fragment.setEventsListener(eventsListener);
                eventsListener.onButtonClicked(fragment);
            }
//            ListEventsFragment fragment = ListEventsFragment.newInstance();
//            getChildFragmentManager().beginTransaction()
//                    .add(R.id.frame_home_fragment, fragment, ListObjectsFragment.class.getName())
//                    .addToBackStack(HomeFragment.class.getName())
//                    .commit();
        });

//        List<Place> events = new ArrayList<>();
//        events.add(new Place(2,"asdasd", 2,"s","s", "s",
//                new User("Sdad","s","s","s"),new KindSport(1,"sdad","asddasd"),null,"89274502477" ));
//        placeAdapter.setmPlaceList(events);
        ApiService service = new ApiService(getActivity());
        List<Place> placeList = service.getPlaces(getArguments().getInt("idKind"), "Kazan", placeAdapter);
        rvEvents.setAdapter(placeAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Constants.DATA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setPrompt("Сортировать по...");
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("POSITION "+position);
                SortService sort = new SortService();
                List<Place> sortPlace = new ArrayList<>();
                switch (position) {
                    case 0: sortPlace = sort.sortPlaces(placeList, "name", SortService.ASC);
                    case 1:sortPlace = sort.sortPlaces(placeList, "price", SortService.ASC);
                    case 2:sortPlace = sort.sortPlaces(placeList, "rating", SortService.ASC);
                }
                placeAdapter.setmPlaceList(sortPlace);
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
//    public interface EventsListener {
//        void onButtonClick(Fragment nextFragment);
//    }
//    public void setEventsListener(EventsListener eventsListener) {
//        this.eventsListener = eventsListener;
//    }

    public void setEventsListener(EventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }

    @Override
    public void notifyData() {
        setVisible();
    }

    public void setVisible() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            rvEvents.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            rvEvents.setVisibility(View.GONE);
        }
    }
}
