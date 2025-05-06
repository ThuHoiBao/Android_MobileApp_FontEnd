package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        // Set username, review content, review date
        holder.userNameTextView.setText(review.getUserName());
        holder.reviewContentTextView.setText(review.getReviewContent());
        holder.reviewDateTextView.setText(review.getReviewDate());

        // Set up rating with ImageView stars
        setRatingStars(holder, review.getRating());

        // Set up RecyclerView for images
        holder.reviewImagesRecyclerView.setLayoutManager(
                new LinearLayoutManager(holder.reviewImagesRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        ReviewImagesAdapter imagesAdapter = new ReviewImagesAdapter(review.getReviewImages());
        holder.reviewImagesRecyclerView.setAdapter(imagesAdapter);
    }

    private void setRatingStars(ReviewViewHolder holder, float rating) {
        // Set all stars to empty initially
        holder.star1.setImageResource(R.drawable.starnonfill);
        holder.star2.setImageResource(R.drawable.starnonfill);
        holder.star3.setImageResource(R.drawable.starnonfill);
        holder.star4.setImageResource(R.drawable.starnonfill);
        holder.star5.setImageResource(R.drawable.starnonfill);

        // Fill the stars based on the rating
        if (rating >= 1) holder.star1.setImageResource(R.drawable.starfill);
        if (rating >= 2) holder.star2.setImageResource(R.drawable.starfill);
        if (rating >= 3) holder.star3.setImageResource(R.drawable.starfill);
        if (rating >= 4) holder.star4.setImageResource(R.drawable.starfill);
        if (rating >= 5) holder.star5.setImageResource(R.drawable.starfill);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, reviewContentTextView, reviewDateTextView;
        ImageView star1, star2, star3, star4, star5;
        RecyclerView reviewImagesRecyclerView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
            reviewContentTextView = itemView.findViewById(R.id.reviewContentTextView);
            reviewDateTextView = itemView.findViewById(R.id.reviewDateTextView);
            reviewImagesRecyclerView = itemView.findViewById(R.id.reviewImagesRecyclerView);
        }
    }
}
