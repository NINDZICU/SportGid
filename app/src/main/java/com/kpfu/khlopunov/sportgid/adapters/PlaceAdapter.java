package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.fragments.NotifyFragment;
import com.kpfu.khlopunov.sportgid.models.Place;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 17.12.2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private Context context;
    private List<Place> mPlaceList;
    private PlaceListener mPlaceListener;
//    private NotifyFragment notifyFragment;

    public PlaceAdapter(Context context) {
        this.context = context;
        mPlaceList = Collections.emptyList();
//        this.notifyFragment = notifyFragment;
    }

    public void setmPlaceList(List<Place> mPlaceList) {
        this.mPlaceList = mPlaceList;
        notifyDataSetChanged();
    }

    @Override
    public PlaceAdapter.PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new PlaceAdapter.PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceAdapter.PlaceViewHolder holder, int position) {
        final Place place = mPlaceList.get(position);
        holder.placeName.setText(place.getTitle());
        holder.placeAddress.setText(place.getAddress());
        holder.placeRaiting.setText(String.valueOf(place.getRating()));
        System.out.println("PHOTOO "+place.getPhoto());
        Glide
                .with(context)
                .load(place.getPhoto())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.placeImage);
//        holder.placePrice.setText("р.");        //TODO Сказать Нурику
        holder.placePrice.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v->{
            if(mPlaceListener!=null){
                mPlaceListener.onAlarmClick(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private ImageView placeImage;
        private TextView placeName;
        private TextView placeAddress;
        private TextView placePrice;
        private TextView placeRaiting;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.image_event);
            placeName = itemView.findViewById(R.id.tv_name_event);
            placeAddress = itemView.findViewById(R.id.tv_address_event);
            placePrice = itemView.findViewById(R.id.tv_price_event);
            placeRaiting = itemView.findViewById(R.id.tv_raiting_event);
        }
    }

    public interface PlaceListener {
        void onAlarmClick(Place place);
    }



    public void setmPlaceListener(PlaceListener mPlaceListener) {
        this.mPlaceListener = mPlaceListener;
    }

    public List<Place> getmPlaceList() {
        return mPlaceList;
    }

//    public void notifyData(){
//        notifyDataSetChanged();
//        notifyFragment.notifyData();
//    }
}
