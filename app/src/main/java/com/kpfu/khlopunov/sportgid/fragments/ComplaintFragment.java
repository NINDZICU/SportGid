package com.kpfu.khlopunov.sportgid.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

/**
 * Created by hlopu on 06.04.2018.
 */

public class ComplaintFragment extends DialogFragment implements ApiCallback{
    public static final String COMPLAINT_FOR_OBJECT ="COMPLAINT_FOR_OBJECT";
    public static final String COMPLAINT_FOR_PLACE ="COMPLAINT_FOR_PLACE";
    public static final String COMPLAINT_FOR_EVENT ="COMPLAINT_FOR_EVENT";
    private boolean[] checked;
    private View editTextComplaint;
    private Context context;

    public static ComplaintFragment newInstance(Context context, int idObject, String TYPE){
        Bundle bundle = new Bundle();
        bundle.putString(COMPLAINT_FOR_OBJECT, TYPE);
        bundle.putInt("idObject", idObject);
        ComplaintFragment fragment = new ComplaintFragment();
        fragment.setArguments(bundle);
        fragment.setContext(context);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String [] arrayComplains = getResources().getStringArray(R.array.type_complaints);
        checked = new boolean[arrayComplains.length];
        for (int i = 0; i < checked.length; i++) {
            checked[i] = false;
        }
//        List<Integer> selected = new ArrayList<>();
        editTextComplaint = new EditText(getActivity());
//        selected.add(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Пожаловаться")
                .setMultiChoiceItems(R.array.type_complaints, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checked[which] = isChecked;
//                        selected.clear();
//                        selected.add(which);
                    }
                })
                .setMessage("Опишите подробнее:")
                .setView(editTextComplaint)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title ="";
                        for (int i = 0; i < checked.length; i++) {
                            if(checked[i]) title+=arrayComplains[i]+", ";
                        }
                        ApiService apiService = new ApiService(getActivity());
                        apiService.sendComplaint(getArguments().getInt("idObject"),
                                SharedPreferencesProvider.getInstance(getActivity()).getUserTokken(),
                                ((EditText)editTextComplaint).getText().toString(), title,ComplaintFragment.this);
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
    public void callback(Object object) {
        if ((Boolean) object == Boolean.TRUE) {
            Toast.makeText(context, "Жалоба успешно отправлена", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, "Не удалось отправить жалобу", Toast.LENGTH_SHORT).show();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
