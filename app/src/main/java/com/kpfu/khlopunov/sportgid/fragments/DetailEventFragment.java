package com.kpfu.khlopunov.sportgid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.MemberListAdapter;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hlopu on 14.12.2017.
 */

public class DetailEventFragment extends Fragment implements ApiCallback {
    private Context context;
    private List<User> users;
    private ImageView ivPhotoEvent;
    private TextView tvCountJoin;
    private TextView tvCountMember;
    private TextView tvNameEvent;
    private TextView tvAddressEvent;
    private Button btnJoin;
    private ProgressBar pbJoin;
    private TextView tvPrice;
    private TextView tvDescription;
    private TextView tvOrganizer;
    private TextView tvKindSport;
    private RecyclerView rvMembers;
    private MemberListAdapter memberListAdapter;
    private EventsListener eventsListener;

    public static DetailEventFragment newInstance(Event event, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        DetailEventFragment fragment = new DetailEventFragment();
        fragment.setContext(context);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);

        Event event = (Event) getArguments().getSerializable("event");
        int id = getArguments().getInt("id");
        Glide
                .with(getContext())
                .load(event.getPhoto())
                .apply(RequestOptions.fitCenterTransform())
                .into(ivPhotoEvent);
        ApiService apiService = new ApiService(getActivity());
//        apiService.getEvent(event.getId(), DetailEventFragment.this);
        tvCountJoin.setText(event != null ? Integer.toString(event.getMembers().size()) : "0");
        tvCountMember.setText(String.valueOf(event.getMaxOfMembers()));
        tvNameEvent.setText(event.getName());
        tvAddressEvent.setText(event.getAddress()!=null? event.getAddress() : "");
        tvPrice.setText(event.getPrice());
        tvDescription.setText(event.getDescription());
        //TODO У Нурика нет в бд
//        tvOrganizer.setText(event.getAvtor().getName());
        tvOrganizer.setText(" USER");
        //TODO У Нурика нет
//        tvKindSport.setText(event.getKindSport().getName());
        tvKindSport.setText(" Test");
        rvMembers.setLayoutManager(new LinearLayoutManager(getActivity()));
        users = new ArrayList<>();
        memberListAdapter = new MemberListAdapter(getActivity(), users);
        memberListAdapter.setmUsers(event.getMembers());
        rvMembers.setAdapter(memberListAdapter);


        btnJoin.setOnClickListener(v -> {
            if(event.isIs_subscribed()){
                btnJoin.setText("Не пойду");
                apiService.unsubscribeEvent(event.getId(), SharedPreferencesProvider.getInstance(context).getUserTokken(),
                        DetailEventFragment.this);
                setVisiblePB(View.VISIBLE);
            }
            else {
                if (SharedPreferencesProvider.getInstance(context).getUserTokken() != null) {
                    apiService.subscribeEvent(event.getId(), SharedPreferencesProvider.getInstance(context).getUserTokken(),
                            DetailEventFragment.this);
                    setVisiblePB(View.VISIBLE);
                } else {
                    //TODO открыть окно авторизации
                }
            }
        });
    }

    public void bind(View view) {

        ivPhotoEvent = view.findViewById(R.id.iv_event_detail);
        tvCountJoin = view.findViewById(R.id.tv_signed_up);
        tvCountMember = view.findViewById(R.id.tv_count_members);
        tvNameEvent = view.findViewById(R.id.tv_detail_name_event);
        tvAddressEvent = view.findViewById(R.id.tv_detail_address);
        btnJoin = view.findViewById(R.id.btn_join);
        tvPrice = view.findViewById(R.id.tv_detail_price);
        tvDescription = view.findViewById(R.id.tv_detail_event_descr);
        tvOrganizer = view.findViewById(R.id.tv_detail_organizer);
        tvKindSport = view.findViewById(R.id.tv_detail_kind_sport);
        rvMembers = view.findViewById(R.id.rv_event_list_member);
        pbJoin = view.findViewById(R.id.pb_join);
    }

    public void setEventsListener(EventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }

    @Override
    public void callback(Object object) {
        if (object instanceof Boolean) {
            if((Boolean) object) {
                users.add(SharedPreferencesProvider.getInstance(context).getUser());
                memberListAdapter.notifyDataSetChanged();
                btnJoin.setText("Не пойду");
                tvCountJoin.setText(Integer.valueOf(tvCountJoin.getText().toString())+1);
                setVisiblePB(View.GONE);
            }
            else {
                users.remove(SharedPreferencesProvider.getInstance(context).getUser());
                memberListAdapter.notifyDataSetChanged();
                btnJoin.setText("Присоединиться");
                tvCountJoin.setText(Integer.valueOf(tvCountJoin.getText().toString())-1);
                setVisiblePB(View.GONE);
            }
        }
        else if (object != null) {
            Event event = (Event) object;
//            tvCountJoin.setText("2");
            //TODO photo


        } else {
            Toast.makeText(getActivity(), "Не удалось загрузить данные", Toast.LENGTH_SHORT).show();
        }
        setVisiblePB(View.GONE);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setVisiblePB(int visibility){
        if(visibility == View.VISIBLE){
            pbJoin.setVisibility(View.VISIBLE);
            btnJoin.setVisibility(View.GONE);
        }
        else if(visibility == View.GONE){
            pbJoin.setVisibility(View.GONE);
            btnJoin.setVisibility(View.VISIBLE);
        }
    }
}
