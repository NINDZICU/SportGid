package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.models.KindSport;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 17.12.2017.
 */

public class InteresAdapter extends RecyclerView.Adapter<InteresAdapter.InteresViewHolder> {
    private Context context;
    private List<KindSport> kindSportList;

    public InteresAdapter(Context context) {
        this.context = context;
        this.kindSportList = Collections.emptyList();
    }

    @Override
    public InteresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.interes_item, parent, false);
        return new InteresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InteresViewHolder holder, int position) {
        final KindSport kindSport = kindSportList.get(position);
        //todo сделать подгрузку иконки
    }

    @Override
    public int getItemCount() {
        return kindSportList.size();
    }

    class InteresViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnInteres;

        public InteresViewHolder(View itemView) {
            super(itemView);
            btnInteres = itemView.findViewById(R.id.btn_interes_item);
        }
    }

    public void setKindSportList(List<KindSport> kindSportList) {
        this.kindSportList = kindSportList;
    }
}
