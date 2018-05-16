package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.activities.AddEventActivity;
import com.kpfu.khlopunov.sportgid.activities.AddPlaceActivity;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 15.05.2018.
 */

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyEventViewHolder> implements ApiCallback {
    public static final String DELETE_EVENT_SUCCESS = "230";
    public static final String DELETE_PLACE_SUCCESS = "231";
    public static final String DELETE_FAILURE = "232";
    private Context context;
    private List<Event> events;
    private List<Place> places;

    private EventListener mEventListener;
    private PlaceListener placeListener;
    private ProgressBar progressBar;
    private ImageButton btnDelete;
    private ImageButton btnEdit;

    public MyEventsAdapter(Context context) {
        this.context = context;
        events = Collections.emptyList();
        places = Collections.emptyList();
    }

    @Override
    public MyEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_event_item, parent, false);
        return new MyEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyEventViewHolder holder, int position) {
        ApiService apiService = new ApiService(context);
        progressBar = holder.progressBar;
        btnDelete = holder.btnDelete;
        btnEdit = holder.btnEdit;

        if (events.size() != 0) {
            Event event = events.get(position);
            Glide
                    .with(context)
                    .load(event.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.ivPhoto);

            holder.tvName.setText(event.getName());
            holder.tvAdress.setText(event.getAddress());

            holder.btnDelete.setOnClickListener(v -> {
                apiService.deleteEvent(event.getId(), SharedPreferencesProvider.getInstance(context).getUserTokken(),
                        MyEventsAdapter.this);
                setVisiblePB(View.VISIBLE);
            });
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddEventActivity.class);
                intent.putExtra("EDIT_EVENT", event);
                context.startActivity(intent);
//                setVisiblePB(View.VISIBLE);
            });
            holder.itemView.setOnClickListener(v -> {
                mEventListener.onAlarmClick(event);
            });
        }
        if (places.size() != 0) {
            Place place = places.get(position);
            Glide
                    .with(context)
                    .load(place.getPhoto())
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.ivPhoto);

            holder.tvName.setText(place.getTitle());
            holder.tvAdress.setText(place.getAddress());

            holder.btnDelete.setOnClickListener(v -> {
                apiService.deletePlace(place.getId(), SharedPreferencesProvider.getInstance(context).getUserTokken(),
                        MyEventsAdapter.this);

                setVisiblePB(View.VISIBLE);
            });
            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddPlaceActivity.class);
                intent.putExtra("EDIT_PLACE", place);
                context.startActivity(intent);
//                setVisiblePB(View.VISIBLE);
            });
            holder.itemView.setOnClickListener(v -> {
                placeListener.onAlarmClick(place);
            });
        }
    }

    @Override
    public int getItemCount() {
        return events.size() != 0 ? events.size() : places.size();
    }

    @Override
    public void callback(Object object) {
        if (object instanceof String) {
            String result = (String) object;
            switch (result.substring(0, 3)) {
                case DELETE_EVENT_SUCCESS:
                    Toast.makeText(context, "Удаление успешно", Toast.LENGTH_SHORT).show();
                    for (Event event : events) {
                        int id = Integer.valueOf(result.substring(3, result.length()));
                        if (event.getId() == id) {
                            events.remove(event);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    break;
                case DELETE_PLACE_SUCCESS:
                    Toast.makeText(context, "Удаление успешно", Toast.LENGTH_SHORT).show();
                    for (Place place : places) {
                        int id = Integer.valueOf(result.substring(3, result.length()));
                        if (place.getId() == id) {
                            places.remove(place);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    break;
                case DELETE_FAILURE:
                    break;
            }
            setVisiblePB(View.GONE);
        }
    }

    public class MyEventViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private TextView tvName;
        private TextView tvAdress;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private ProgressBar progressBar;

        public MyEventViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.image_my_event);
            tvName = itemView.findViewById(R.id.tv_name_my_event);
            tvAdress = itemView.findViewById(R.id.tv_address_my_event);
            btnEdit = itemView.findViewById(R.id.btn_edit_my_event);
            btnDelete = itemView.findViewById(R.id.btn_delete_my_event);
            progressBar = itemView.findViewById(R.id.pb_profile_delete);
        }
    }

    /*
    Использовать только один из сетов
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void setmEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    public void setPlaceListener(PlaceListener placeListener) {
        this.placeListener = placeListener;
    }

    public interface EventListener {
        void onAlarmClick(Event event);
    }

    public interface PlaceListener {
        void onAlarmClick(Place place);
    }

    public void setVisiblePB(int visiblePB) {
        if (visiblePB == View.GONE) {
            progressBar.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else if (visiblePB == View.VISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }
}
