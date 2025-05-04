package com.example.retofit2.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retofit2.R;

import java.util.List;

public class FeedbackImagesAdapter extends RecyclerView.Adapter<FeedbackImagesAdapter.ImageViewHolder> {

    private Context context;
    private List<String> images;

    public FeedbackImagesAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Tải hình ảnh vào ImageView trong RecyclerView
        Glide.with(context).load(images.get(position)).into(holder.imageView);

        // Thêm sự kiện click vào mỗi ảnh
        holder.imageView.setOnClickListener(v -> {
            // Gọi phương thức showImageDialog khi nhấn vào ảnh
            showImageDialog(images.get(position));
        });
    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    // Tạo một dialog hiển thị ảnh lớn
    private void showImageDialog(String imageUrl) {
        // Tạo dialog với layout chứa ImageView
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_image_view, null);

        // Tìm ImageView trong layout
        ImageView imageView = dialogView.findViewById(R.id.fullScreenImage);

        // Sử dụng Glide để tải và hiển thị ảnh trong ImageView
        Glide.with(context).load(imageUrl).into(imageView);

        // Tạo và hiển thị Dialog
        builder.setView(dialogView)
                .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss()) // Nút "Đóng"
                .create()
                .show();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feedbackImageView);
        }
    }
}
