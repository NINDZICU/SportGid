package com.kpfu.khlopunov.sportgid.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.activities.AddEventActivity;
import com.kpfu.khlopunov.sportgid.activities.AddPlaceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 17.12.2017.
 */

public class AddEventDialogFragment extends DialogFragment implements View.OnClickListener {
//    Button btnCancel;
//    Button btnOk;
//    RadioButton rbEvent;
//    RadioButton rbPlace;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.add_profile_fragment, null);
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getDialog().setTitle("Добавить");
//        btnCancel = view.findViewById(R.id.btn_dialog_add_cancel);
//        btnOk = view.findViewById(R.id.btn_dialog_add_ok);
//        rbEvent = view.findViewById(R.id.rb_add_event);
//        rbEvent.setChecked(true);
//        rbPlace = view.findViewById(R.id.rb_add_place);
//
//        btnCancel.setOnClickListener(v -> {
//            dismiss();
//        });
//        btnOk.setOnClickListener(v->{
//        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<Integer> selected = new ArrayList<>();
        selected.add(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Добавить")
                .setSingleChoiceItems(R.array.event, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected.clear();
                        selected.add(which);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selected.get(0) == 0) {
                            Intent intent = new Intent(getActivity(), AddPlaceActivity.class);
                            startActivity(intent);
                        } else if (selected.get(0) == 1) {
                            Intent intent = new Intent(getActivity(), AddEventActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onClick(View v) {

    }
}
