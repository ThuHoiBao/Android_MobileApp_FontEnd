package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;
import com.example.retofit2.dto.requestDTO.CategoryRequestDTO;

import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;
import com.example.retofit2.dto.requestDTO.OrderStatusRequestDTO;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.OrderStatusViewHolder> {

    private Context context;
    private List<OrderStatusRequestDTO> statusList;

    public OrderStatusAdapter(Context context, List<OrderStatusRequestDTO> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public OrderStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_status_item, parent, false);
        return new OrderStatusViewHolder(view);
    }
    // Tạo interface
    public interface OnStatusClickListener {
        void onStatusClick(String statusName);
    }

    private OnStatusClickListener listener;

    // Hàm set Listener từ ngoài vào
    public void setOnStatusClickListener(OnStatusClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusViewHolder holder, int position) {
        OrderStatusRequestDTO status = statusList.get(position);
        holder.name.setText(status.getNameOrderStatus());

        int imageResId = context.getResources().getIdentifier(
                status.getImageOrderStatus(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.image.setImageResource(imageResId);
        } else {
            holder.image.setImageResource(R.drawable.delivery); // fallback
        }

        holder.card.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStatusClick(status.getNameOrderStatus());
            }
        });

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public static class OrderStatusViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        CardView card;

        public OrderStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.orderStatusName);
            image = itemView.findViewById(R.id.orderStatusImage);
            card = itemView.findViewById(R.id.category_card);
        }
    }
}
