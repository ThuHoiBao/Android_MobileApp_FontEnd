package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductPaymentAdapter extends RecyclerView.Adapter<ProductPaymentAdapter.ProductViewHolder> {
    private List<CardItemDTO> productList;
    public ProductPaymentAdapter(List<CardItemDTO> productList){
        this.productList = productList;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_payment, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CardItemDTO cardItemDTO = productList.get(position);
        Picasso.get()
                .load(cardItemDTO.getProductImage()) // Lấy URL từ CardItemDTO
                .into(holder.productImage);
        holder.productName.setText(cardItemDTO.getProductName());
        holder.productColor.setText(cardItemDTO.getColor());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(cardItemDTO.getPrice());
        holder.productPrice.setText("₫" + formattedPrice);
        holder.productQuantity.setText("x" + cardItemDTO.getQuantity());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName, productPrice, productQuantity, productColor;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productColor = itemView.findViewById(R.id.productColor);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
        }
    }
}
