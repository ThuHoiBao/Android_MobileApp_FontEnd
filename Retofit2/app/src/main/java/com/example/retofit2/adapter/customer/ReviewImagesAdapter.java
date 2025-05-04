package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewImagesAdapter extends RecyclerView.Adapter<ReviewImagesAdapter.ReviewImageViewHolder> {

    private List<String> imageUrls;

    public ReviewImagesAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ReviewImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_image_item, parent, false);
        return new ReviewImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Picasso.get().load(imageUrl).into(holder.reviewImageView); // Dùng Picasso để tải và hiển thị hình ảnh
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ReviewImageViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewImageView;

        public ReviewImageViewHolder(View itemView) {
            super(itemView);
            reviewImageView = itemView.findViewById(R.id.reviewImageView); // ID của ImageView trong review_image_item.xml
        }
    }
}
