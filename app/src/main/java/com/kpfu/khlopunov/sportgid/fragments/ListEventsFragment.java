package com.kpfu.khlopunov.sportgid.fragments;

import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.kpfu.khlopunov.sportgid.Constants;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.EventAdapter;
import com.kpfu.khlopunov.sportgid.custom.NoDefaultSpinner;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;
import com.kpfu.khlopunov.sportgid.service.SortService;

import io.apptik.widget.MultiSlider;

/**
 * Created by hlopu on 14.12.2017.
 */

public class ListEventsFragment extends Fragment implements NotifyFragment, NavigationView.OnNavigationItemSelectedListener{
    private static final int PRICEMAX = 1000;
    private Context context;
    private Button btnObjects;
    private Button btnEvents;
    private NoDefaultSpinner spinnerSort;
    private ImageButton btnFilter;
    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private EventsListener eventsListener;
    private ProgressBar progressBar;
    //TODO Если будет нужен фильтр раскоментить строки здесь и вернуть в верстку drawer
//    private DrawerLayout drawer;
//    private MultiSlider multiSliderPrice;
//    private TextView tvMinPrice;
//    private TextView tvMaxPrice;
//    private Button btnFilterSearch;


    public static ListEventsFragment newInstance(int idKind, Context context) {
        Bundle bundle = new Bundle();
        bundle.putInt("idKind", idKind);
        ListEventsFragment fragment = new ListEventsFragment();
        fragment.setContext(context);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_objects_events_drawer_layout, container, false);
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
        setVisiblePB(View.VISIBLE);

        btnFilter.setVisibility(View.GONE);
        /**
        btnFilterSearch = view.findViewById(R.id.btn_filter_search);
        tvMaxPrice = view.findViewById(R.id.tv_filter_max_price);
        tvMinPrice = view.findViewById(R.id.tv_filter_min_price);
        multiSliderPrice = view.findViewById(R.id.range_slider);
        drawer = view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = view.findViewById(R.id.drawer_layout);
         */

        rvEvents.setLayoutManager(new LinearLayoutManager(context));
        eventAdapter = new EventAdapter(context, ListEventsFragment.this);

        ApiService apiService = new ApiService(context);
    /**
        multiSliderPrice.setMax(PRICEMAX);
        tvMinPrice.setText("0");
        tvMaxPrice.setText(String.valueOf(PRICEMAX));
        multiSliderPrice.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSliderPrice, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    tvMinPrice.setText(String.valueOf(value));
                } else if (thumbIndex == 1) {
                    tvMaxPrice.setText(String.valueOf(value));
                }
            }
        });
        btnFilter.setOnClickListener(v -> {
            drawer.openDrawer(GravityCompat.START);
        });
        btnFilterSearch.setOnClickListener(v -> {
            setVisiblePB(View.VISIBLE);
            apiService.getEventsByPrice(eventAdapter, getArguments().getInt("idKind"),
                    tvMinPrice.getText().toString(), tvMaxPrice.getText().toString());
            drawer.closeDrawer(GravityCompat.START);
        });
*/
        btnObjects.setOnClickListener(v -> {
            if (eventsListener != null) {
                ListObjectsFragment fragment = ListObjectsFragment.newInstance(getArguments().getInt("idKind"));
                fragment.setEventsListener(eventsListener);
                eventsListener.onButtonClicked(fragment);
            }
//            getChildFragmentManager().beginTransaction()
//                    .add(R.id.frame_home_fragment, fragment, ListObjectsFragment.class.getName())
//                    .addToBackStack(HomeFragment.class.getName())
//                    .commit();
        });

        eventAdapter.setmEventListener(event -> {
            DetailEventFragment fragment = DetailEventFragment.newInstance(event, context);
            eventsListener.onButtonClicked(fragment);
        });

        apiService.getEvents(getArguments().getInt("idKind"),
                SharedPreferencesProvider.getInstance(context).getCity(), eventAdapter);
        rvEvents.setAdapter(eventAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Constants.DATA);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setPrompt("Сортировать по...");
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortService service = new SortService();
//                service.sortEvents();
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

    @Override
    public void notifyData() {
        setVisiblePB(View.GONE);
    }

    public void setVisiblePB(int visibility) {
        if (visibility == View.GONE) {
            progressBar.setVisibility(View.GONE);
            rvEvents.setVisibility(View.VISIBLE);
        } else if(visibility == View.VISIBLE){
            progressBar.setVisibility(View.VISIBLE);
            rvEvents.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
