package com.example.retofit2.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private Integer[] imageResources;
    private OnImageClickListener listener;
    private int selectedPosition = 0;

    public ImageAdapter(Context context, Integer[] imageResources, OnImageClickListener listener) {
        this.context = context;
        this.imageResources = imageResources;
        this.listener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(imageResources[position]);

        int adapterPosition = holder.getAdapterPosition();

        if (adapterPosition == selectedPosition) {
            holder.imageView.setAlpha(1f);
        } else {
            holder.imageView.setAlpha(0.5f);
        }

        holder.imageView.setOnClickListener(v -> {
            selectedPosition = adapterPosition;
            listener.onImageClick(adapterPosition);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return imageResources.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
        }
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView khi thay đổi ảnh được chọn
    }
}
