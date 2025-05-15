package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        cartAdapter = new CartAdapter(cartItems, recyclerView, totalAmountText, new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onItemClick(CardItemDTO item) {
                String productName = item.getProductName();
                Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                intent.putExtra("productName", productName);
                startActivity(intent);
                finish();
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.adminNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        //   Long userId = getIntent().getLongExtra("userId", -1);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(CartActivity.this, CustomerHomeActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                    return true;
                }
                // Tạm thời comment 2 chức năng này
                else if (id == R.id.nav_orders) {
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                    return true;
                }
                else if (id == R.id.nav_account) {
                    Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                    return true;
                }
                else if(id == R.id.nav_chat){
                    Intent intent = new Intent(CartActivity.this, ChatBoxActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if(id == R.id.nav_cart){
                    Intent intent = new Intent(CartActivity.this, CartActivity.class);
                    startActivity(intent);
                    return true;
                } else if(id == R.id.nav_account){
                    Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

        });

        shippingButton.setOnClickListener(v -> {
            // Handle shipping action
            List<CardItemDTO> selectedItems = new ArrayList<>();
            for(CardItemDTO item : cartItems){
                if(item.isSelected() && !item.isOutOfStockItems()){
                    selectedItems.add(item);
                }
            }
            if(selectedItems.isEmpty()){
                Toast.makeText(CartActivity.this, "Please select at least one item", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putParcelableArrayListExtra("selectedItems", new ArrayList<>(selectedItems));
                startActivity(intent);
            }
        });

        selectAllCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectAllItems(isChecked);  // Cập nhật tất cả các checkbox
        });
        cartAdapter.updateTotalAmount();

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