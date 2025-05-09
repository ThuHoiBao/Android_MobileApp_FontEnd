package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.ProductPaymentAdapter;
import com.example.retofit2.api.IAddressDelivery;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView productsRecycleView;
    private ProductPaymentAdapter productPaymentAdapter;
    private List<CardItemDTO> productList;
    final long userId = SharedPrefManager.getUserId();
    private TextView fullNameText, phoneNumberText, addressText;
    private TextView totalAmountText, discountAmountText;

    private ImageButton backIcon, nextButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        productsRecycleView = findViewById(R.id.productRecyclerView);
        backIcon = findViewById(R.id.backIcon);
        fullNameText = findViewById(R.id.nameText);
        phoneNumberText = findViewById(R.id.phoneText);
        addressText = findViewById(R.id.addressText);
        nextButton = findViewById(R.id.nextButton);
        productsRecycleView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách sản phẩm mẫu
        productList = getProductList(); // Lấy danh sách sản phẩm
        productPaymentAdapter = new ProductPaymentAdapter(productList);
        productsRecycleView.setAdapter(productPaymentAdapter);

        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        });

        //Lay thong tin thanh toans
        getDefaultDeliveryAddress(userId);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, AddressDeliveryActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getDefaultDeliveryAddress(long userId) {
        IAddressDelivery apiService = APIRetrofit.getRetrofitInstance().create(IAddressDelivery.class);
        Call<AddressDeliveryDTO> call = apiService.getDefaultAddress(userId);
        call.enqueue(new Callback<AddressDeliveryDTO>() {

            @Override
            public void onResponse(Call<AddressDeliveryDTO> call, Response<AddressDeliveryDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddressDeliveryDTO address = response.body(); // Giả sử lấy địa chỉ đầu tiên
                    Log.d("name", "Extracted Address Id: " + address.getFullName());
                    Log.d("phone", "Extracted Address Id: " + address.getPhoneNumber());
                    Log.d("address", "Extracted Address Id: " + address.getAddress());
                    fullNameText.setText(address.getFullName());
                    phoneNumberText.setText(address.getPhoneNumber());
                    addressText.setText(address.getAddress());
                } else {
                    Log.e("PaymentActivity", "Failed to fetch default address.");
                }
            }

            @Override
            public void onFailure(Call<AddressDeliveryDTO> call, Throwable t) {

            }
        });
    }





    public List<CardItemDTO> getProductList() {
        List<CardItemDTO> productList = new ArrayList<>();

//        CardItemDTO cardItemDTO1 = new CardItemDTO("Đồng hồ nam Led LP22","Blue",
//                23000,
//                1);
////        cardItemDTO1.setImageUrl("https://cdn.tgdd.vn/Products/Images/42/281570/iphone-15-hong-thumb-1-600x600.jpg");
//
//        CardItemDTO cardItemDTO2 = new CardItemDTO("Đồng hồ nam Led LP22","Blue",
//                23000,
//                1);
//        cardItemDTO1.setImageUrl("https://cdn.tgdd.vn/Products/Images/42/281570/iphone-15-hong-thumb-1-600x600.jpg");

//        productList.add(cardItemDTO1);
//
//
//        productList.add(cardItemDTO2);
        return productList;
    }
}
