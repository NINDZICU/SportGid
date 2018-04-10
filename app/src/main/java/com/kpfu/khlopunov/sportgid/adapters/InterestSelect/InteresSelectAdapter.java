package com.kpfu.khlopunov.sportgid.adapters.InterestSelect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.models.KindSport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 18.12.2017.
 */

public class InteresSelectAdapter extends InteresSelect {
    private List<KindSport> selected;

    public InteresSelectAdapter(Context context) {
        super(context);
        selected = new ArrayList<>();
    }

    @Override
    public InteresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_interes_item, parent, false);
        return new InteresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final KindSport kindSport = kindSports.get(position);
        InteresSelectAdapter.InteresViewHolder viewHolder = (InteresSelectAdapter.InteresViewHolder) holder;
        viewHolder.tvInteresName.setText(kindSport.getName());
        View.OnClickListener l = v -> {
            if (viewHolder.cbInteres.isChecked()) selected.add(kindSport);
            else selected.remove(kindSport);
        };
        viewHolder.cbInteres.setOnClickListener(l);
        viewHolder.itemView.setOnClickListener(l);
    }

    @Override
    public int getItemCount() {
        return kindSports.size();
    }

    class InteresViewHolder extends RecyclerView.ViewHolder {
        TextView tvInteresName;
        CheckBox cbInteres;

        public InteresViewHolder(View itemView) {
            super(itemView);
            tvInteresName = itemView.findViewById(R.id.tv_name_interes_item);
            cbInteres = itemView.findViewById(R.id.checkbox_interes_item);
        }
    }
    @Override
    public List<KindSport> getSelected() {
        return selected;
    }

}
