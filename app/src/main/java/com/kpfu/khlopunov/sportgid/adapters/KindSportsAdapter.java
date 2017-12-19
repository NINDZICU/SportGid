package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.fragments.NotifyFragment;
import com.kpfu.khlopunov.sportgid.models.KindSport;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 12.12.2017.
 */

public class KindSportsAdapter extends RecyclerView.Adapter<KindSportsAdapter.KindSportViewHolder> {
    private Context context;
    private List<KindSport> mKindSports;
    private KindSportListener mKindSportListener;
    private NotifyFragment notifyFragment;

    public KindSportsAdapter(Context context, NotifyFragment notifyFragment) {
        this.context = context;
        mKindSports = Collections.emptyList();
        this.notifyFragment = notifyFragment;
    }

    public void setKindSports(List<KindSport> kindSports) {
        this.mKindSports = kindSports;
    }

    @Override
    public KindSportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.kind_sport_item,
                parent,
                false);
        return new KindSportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KindSportViewHolder holder, int position) {
        final KindSport kindSport = mKindSports.get(position);
        holder.tvKindSport.setText(kindSport.getName());
        holder.tvKindSport.setOnClickListener(v -> {
            if (mKindSportListener != null) {
                mKindSportListener.onAlarmClick(kindSport);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mKindSports.size();
    }

    public class KindSportViewHolder extends RecyclerView.ViewHolder {
        Button tvKindSport;

        public KindSportViewHolder(View itemView) {
            super(itemView);
            tvKindSport = itemView.findViewById(R.id.btn_kind_sport);
        }
    }

    public interface KindSportListener {
        void onAlarmClick(KindSport kindSport);
    }

    public void setmKindSportListener(KindSportListener mKindSportListener) {
        this.mKindSportListener = mKindSportListener;
    }
    public void notifyData(){
        notifyDataSetChanged();
        notifyFragment.notifyData();
    }
}
