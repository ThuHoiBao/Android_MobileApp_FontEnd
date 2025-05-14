package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import com.bumptech.glide.Glide;
import com.example.retofit2.R;
import com.example.retofit2.dto.requestDTO.ProductRequestDTO;

import java.util.List;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder> {
    private List<ProductRequestDTO> productList;
    private OnProductClickListener productClickListener;

    public HomeProductAdapter(List<ProductRequestDTO> productList, OnProductClickListener productClickListener) {
        this.productList = productList;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_home_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductRequestDTO product = productList.get(position);
        holder.productName.setText(product.getProductName());

        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Define the decimal format
        String formattedProductPrice = decimalFormat.format(product.getPrice()); // Format the product price
        holder.productPrice.setText(formattedProductPrice + "₫"); // Set the formatted price to the view

        holder.productSold.setText("Đã bán: " + product.getSoldQuantity());

        // Kiểm tra xem danh sách imageProducts có ít nhất 1 phần tử hay không
        if (product.getImageProducts() != null && !product.getImageProducts().isEmpty()) {
            // Lấy ảnh đầu tiên trong danh sách
            String image = product.getImageProducts().get(0);

            try {
                Glide.with(holder.itemView.getContext())
                        .load(image)  // Chỗ này có thể là URL hoặc Base64 string
                        .placeholder(R.drawable.phone)  // Ảnh tạm khi load
                        .error(R.drawable.phone)  // Ảnh lỗi nếu sai
                        .into(holder.productImage);
            } catch (Exception e) {
                holder.productImage.setImageResource(R.drawable.livechat);  // Khi có lỗi, hiển thị ảnh mặc định
            }
        } else {
            holder.productImage.setImageResource(R.drawable.livechat);  // Nếu không có ảnh, hiển thị ảnh mặc định
        }

        holder.itemView.setOnClickListener(v -> {
            if(productClickListener != null){
                productClickListener.onProductClick(product.getProductName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSold;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productSold = itemView.findViewById(R.id.product_sold);
        }
    }
    public void setListenerList(List<ProductRequestDTO> iconModelList) {
        this.productList = iconModelList;
        notifyDataSetChanged();  // Thông báo thay đổi dữ liệu
    }


    public interface OnProductClickListener{
        void onProductClick(String productName);
    }
}

