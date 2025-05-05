package com.example.retofit2.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageViewHolder2Adapter extends RecyclerView.Adapter<ImageViewHolder2Adapter.ImageViewHolder> {
    private Context context;
    private List<String> imageUrls;

    public ImageViewHolder2Adapter(Context context,  List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout for each item in ViewPager2
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String url = imageUrls.get(position);
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.iphone1) // hình tạm
                .error(R.drawable.error_image)       // khi lỗi
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
