package com.kpfu.khlopunov.sportgid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.ReviewAdapter;
import com.kpfu.khlopunov.sportgid.models.ApiResult;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.Review;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.kpfu.khlopunov.sportgid.service.ApiService;

import java.util.List;

/**
 * Created by hlopu on 14.12.2017.
 */

public class DetailPlaceFragment extends Fragment implements ApiCallback {
    private ImageView ivPlace;
    private TextView tvRaitng;
    private TextView tvName;
    private TextView tvAddress;
    private Button btnTakeOver;
    private TextView tvPrice;
    private TextView tvDescription;
    private TextView tvContacts;
    private RecyclerView rvReviews;
    private TextView tvReviews;
    private EditText etReview;
    private TextView tvSendReview;
    private TextView tvComplain;
    private ReviewAdapter adapter;

    public static DetailPlaceFragment newInstance(Place place) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        DetailPlaceFragment fragment = new DetailPlaceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_object, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        checkAuthorisation();
        Place place = (Place) getArguments().getSerializable("place");
        Glide
                .with(getContext())
                .load(place.getPhoto())
                .apply(RequestOptions.fitCenterTransform())
                .into(ivPlace);
        tvRaitng.setText(String.valueOf(place.getRating()));
        tvName.setText(place.getTitle());
        tvAddress.setText(place.getAddress());
//        tvPrice.setText(place.getPrice());
        tvDescription.setText(place.getDescription());
        tvContacts.setText(place.getContact());

        rvReviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReviewAdapter(getActivity());
        adapter.setReviewList(place.getReviews());

        rvReviews.setAdapter(adapter);

        btnTakeOver.setOnClickListener(v -> {

        });

        tvSendReview.setOnClickListener(v -> {
            if (etReview.length() == 0)
                Toast.makeText(getActivity(), "Заполите поле", Toast.LENGTH_SHORT).show();
            else {
                ApiService service = new ApiService(getActivity());
                service.addReview(place.getId(), SharedPreferencesProvider.getInstance(getActivity()).getUserTokken(),
                        etReview.getText().toString(), 2, DetailPlaceFragment.this);
            }
        });
        tvComplain.setOnClickListener(v -> {

        });

    }

    private void bind(View view) {
        ivPlace = view.findViewById(R.id.iv_place_detail);
        tvRaitng = view.findViewById(R.id.tv_detail_raiting);
        tvName = view.findViewById(R.id.tv_detail_place_name);
        tvAddress = view.findViewById(R.id.tv_place_address);
        btnTakeOver = view.findViewById(R.id.btn_take_over);
        tvPrice = view.findViewById(R.id.tv_place_price);
        tvDescription = view.findViewById(R.id.tv_detail_place_descr);
        tvContacts = view.findViewById(R.id.tv_place_number);
        rvReviews = view.findViewById(R.id.rv_place_review);
        etReview = view.findViewById(R.id.et_review);
        tvSendReview = view.findViewById(R.id.tv_send_review);
        tvComplain = view.findViewById(R.id.tv_complain);
        tvReviews = view.findViewById(R.id.tv_detail_send_review);
    }

    @Override
    public void callback(Object object) {
        if ((Boolean) object == Boolean.TRUE) {
            Toast.makeText(getActivity(), "Отзыв успешно добавлен!!", Toast.LENGTH_SHORT).show();

            List<Review> reviewList =adapter.getReviewList();       //TODO сделать подгрузку с сервера после добавления или что-то другое
            reviewList.add(new Review(0, 0l, etReview.getText().toString(),4,       //хардкод поменять
                    SharedPreferencesProvider.getInstance(getActivity()).getUser(),null,null  ));
            adapter.setReviewList(reviewList);

            etReview.setText("");
            adapter.notifyDataSetChanged();


        } else
            Toast.makeText(getActivity(), "Не удалось добавить отзыв", Toast.LENGTH_SHORT).show();
    }

    public void checkAuthorisation(){
        if (SharedPreferencesProvider.getInstance(getActivity()).getUserTokken()==null){
            etReview.setVisibility(View.GONE);
            tvSendReview.setVisibility(View.GONE);
            tvReviews.setVisibility(View.GONE);
        }
    }
}
