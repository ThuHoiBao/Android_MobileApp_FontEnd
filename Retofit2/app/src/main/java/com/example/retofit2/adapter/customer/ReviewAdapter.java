package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.dto.requestDTO.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList; // danh sach review

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.userNameTextView.setText(review.getUserName());
        holder.ratingBar.setRating(review.getRating());
        holder.reviewContentTextView.setText(review.getReviewContent());
        holder.reviewDateTextView.setText(review.getReviewDate());

        // Set up RecyclerView for images
        holder.reviewImagesRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.reviewImagesRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        ReviewImagesAdapter imagesAdapter = new ReviewImagesAdapter(review.getReviewImages());
        holder.reviewImagesRecyclerView.setAdapter(imagesAdapter);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, reviewContentTextView, reviewDateTextView;
        RatingBar ratingBar;
        RecyclerView reviewImagesRecyclerView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewContentTextView = itemView.findViewById(R.id.reviewContentTextView);
            reviewDateTextView = itemView.findViewById(R.id.reviewDateTextView);
            reviewImagesRecyclerView = itemView.findViewById(R.id.reviewImagesRecyclerView);
        }
    }
}
