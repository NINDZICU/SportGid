package com.kpfu.khlopunov.sportgid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.fragments.EventsListener;
import com.kpfu.khlopunov.sportgid.models.Review;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 17.12.2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;
    private EventsListener eventsListener;

    public ReviewAdapter(Context context) {
        this.context = context;
        reviewList = Collections.emptyList();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = reviewList.get(position);
        //Todo сделать для картинки
        holder.tvReviewerName.setText(review.getUser().getName());
        holder.tvMark.setText(String.valueOf(review.getRating()));
        holder.tvReview.setText(review.getBody());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReviewer;
        TextView tvReviewerName;
        TextView tvMark;
        TextView tvReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ivReviewer = itemView.findViewById(R.id.iv_reviewer_photo);
            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
            tvMark = itemView.findViewById(R.id.tv_reviewer_mark);
            tvReview = itemView.findViewById(R.id.tv_review);
        }
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setEventsListener(EventsListener eventsListener) {
        this.eventsListener = eventsListener;
    }
}
