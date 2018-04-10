package com.kpfu.khlopunov.sportgid.adapters.InterestSelect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.models.KindSport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 05.04.2018.
 */

public class InteresSelectAdapterRadio extends InteresSelect {
    private KindSport selectedKindSport;
    private int currentPosition = -1;

    public InteresSelectAdapterRadio(Context context) {
        super(context);
    }

    @Override
    public InteresSelectAdapterRadio.InteresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_interes_item_radio, parent, false);
        return new InteresSelectAdapterRadio.InteresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final KindSport kindSport = kindSports.get(position);
        ((InteresSelectAdapterRadio.InteresViewHolder) holder).tvInteresName.setText(kindSport.getName());
        ((InteresViewHolder) holder).rbInteres.setChecked(position == currentPosition);
        View.OnClickListener l = v -> {
            selectedKindSport = kindSport;
            currentPosition = position;
            notifyDataSetChanged();
        };
        ((InteresSelectAdapterRadio.InteresViewHolder) holder).rbInteres.setOnClickListener(l);
        ((InteresViewHolder) holder).itemView.setOnClickListener(l);
    }

    class InteresViewHolder extends RecyclerView.ViewHolder {
        TextView tvInteresName;
        RadioButton rbInteres;

        public InteresViewHolder(View itemView) {
            super(itemView);
            tvInteresName = itemView.findViewById(R.id.tv_name_interes_item);
            rbInteres = itemView.findViewById(R.id.radio_interes_item);
        }
    }

    @Override
    public List<KindSport> getSelected() {
        List<KindSport> selectedKindSports = new ArrayList<>();
        selectedKindSports.add(selectedKindSport);
        return selectedKindSports;
    }

}
