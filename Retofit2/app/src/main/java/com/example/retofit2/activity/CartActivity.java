package com.example.retofit2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.CartAdapter;
import com.example.retofit2.api.ICartAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.utils.SharedPrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox selectAllCheckbox;
    private CartAdapter cartAdapter;
    private List<CardItemDTO> cartItems;
    private TextView totalAmountText;
    private AppCompatButton shippingButton;
    private double totalAmount;
    private ImageButton backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        selectAllCheckbox = findViewById(R.id.selectAllCheckbox);
        totalAmountText = findViewById(R.id.totalAmountText);
        shippingButton = findViewById(R.id.shippingButton);
        backIcon = findViewById(R.id.backIcon);

        cartItems = new ArrayList<>();
        Long userId = SharedPrefManager.getUserId();
        String token = SharedPrefManager.getToken();
        loadCartData(token, userId);
        cartAdapter = new CartAdapter(cartItems, recyclerView, totalAmountText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);


        shippingButton.setOnClickListener(v -> {
            // Handle shipping action
            Toast.makeText(CartActivity.this, "Proceeding to Shipping", Toast.LENGTH_SHORT).show();
        });

        selectAllCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectAllItems(isChecked);  // Cập nhật tất cả các checkbox
        });
        cartAdapter.updateTotalAmount();

        backIcon.setOnClickListener(v -> {
            Intent view = new Intent(CartActivity.this, ProductDetailActivity.class);
            startActivity(view);
            finish();
        });
    }

    public void selectAllItems(boolean isSelected) {
        for (CardItemDTO item : cartItems) {
            if(!item.isOutOfStockItems()){
                item.setSelected(isSelected);
            }

        }
        cartAdapter.notifyDataSetChanged();
        updateTotalAmount();
    }

    private void loadCartData(String token, Long userId) {
        // Sử dụng Retrofit với Bearer Token
        ICartAPI apiService = APIRetrofit.getCartAPIService(token);  // Truyền token vào API service

        // Gọi API để lấy danh sách giỏ hàng
        apiService.getCartItems(userId).enqueue(new Callback<List<CardItemDTO>>() {
            @Override
            public void onResponse(Call<List<CardItemDTO>> call, Response<List<CardItemDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    for (CardItemDTO item : response.body()) {
                        item.setSelected(false);
                        cartItems.add(item);
                    }
                    cartAdapter.notifyDataSetChanged();

                    updateTotalAmount();
                    // Tính toán tổng giá trị giỏ hàng
//                    double totalAmount = 0.0;
//                    for (CardItemDTO item : cartItems) {
//                        totalAmount += item.getPrice() * item.getQuantity();
//                    }

                    totalAmountText.setText("Total Amount: " + 0 + "đ");
                } else {
                    Log.d("loadCart", "Extracted User Id: " + token);
                    Toast.makeText(CartActivity.this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CardItemDTO>> call, Throwable t) {
                Log.d("loadCart", "Extracted User Id: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalAmount(){
        totalAmount = 0.0;
        for(CardItemDTO item : cartItems){
            if(item.isSelected() && !item.isOutOfStockItems()){
                totalAmount += item.getPrice() * item.getQuantity();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###");  // Định dạng giá tr
        String formattedPrice = decimalFormat.format(totalAmount);
        totalAmountText.setText("Total Amount: " + formattedPrice + "đ");

        // Đảm bảo gọi notifyDataSetChanged() sau khi RecyclerView đã hoàn thành layout
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                cartAdapter.notifyDataSetChanged();  // Cập nhật lại RecyclerView sau khi thay đổi dữ liệu
            }
        });
    }
}