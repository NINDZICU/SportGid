package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.models.Event;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 13.12.2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> mEventList;
    private EventListener mEventListener;

    public EventAdapter(Context context) {
        this.context = context;
        mEventList = Collections.emptyList();
    }

    public void setmEventList(List<Event> mEventList) {
        this.mEventList = mEventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        final Event event = mEventList.get(position);
        //TODO сделать set для картинки
        holder.eventName.setText(event.getName());
        holder.eventAddress.setText(event.getAddress());
        holder.eventRaiting.setText(event.getRaiting());
        holder.eventPrice.setText(event.getPrice());

        holder.itemView.setOnClickListener(v->{
            if(mEventListener!=null){
                mEventListener.onAlarmClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private ImageView eventImage;
        private TextView eventName;
        private TextView eventAddress;
        private TextView eventPrice;
        private TextView eventRaiting;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.image_event);
            eventName = itemView.findViewById(R.id.tv_name_event);
            eventAddress = itemView.findViewById(R.id.tv_address_event);
            eventPrice = itemView.findViewById(R.id.tv_price_event);
            eventRaiting = itemView.findViewById(R.id.tv_raiting_event);
        }
    }

    public interface EventListener {
        void onAlarmClick(Event event);
    }

    public EventListener getmEventListener() {
        return mEventListener;
    }

    public void setmEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }
}
