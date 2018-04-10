package com.kpfu.khlopunov.sportgid.adapters.InterestSelect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.kpfu.khlopunov.sportgid.models.KindSport;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 06.04.2018.
 */

public abstract class InteresSelect extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    protected List<KindSport> kindSports;

    InteresSelect(Context context) {
        this.context = context;
        kindSports = Collections.emptyList();
    }

    public abstract List<KindSport> getSelected();

    public void setKindSports(List<KindSport> kindSports) {
        this.kindSports = kindSports;
    }

    @Override
    public int getItemCount() {
        return kindSports.size();
    }
}
