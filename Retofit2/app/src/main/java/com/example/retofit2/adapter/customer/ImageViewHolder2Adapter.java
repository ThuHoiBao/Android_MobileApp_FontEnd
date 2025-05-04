package com.example.retofit2.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;


public class ImageViewHolder2Adapter extends RecyclerView.Adapter<ImageViewHolder2Adapter.ImageViewHolder> {
    private Context context;
    private Integer[] imageResources;

    public ImageViewHolder2Adapter(Context context, Integer[] imageResources) {
        this.context = context;
        this.imageResources = imageResources;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout for each item in ViewPager2
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Set image for each page in ViewPager2
        holder.imageView.setImageResource(imageResources[position]);
    }

    @Override
    public int getItemCount() {
        return imageResources.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
