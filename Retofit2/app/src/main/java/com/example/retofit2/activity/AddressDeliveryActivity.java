package com.example.retofit2.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.AddressDeliveryAdapter;
import com.example.retofit2.adapter.customer.ProductPaymentAdapter;
import com.example.retofit2.api.IAddressDelivery;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;
import com.example.retofit2.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDeliveryActivity extends AppCompatActivity {
    private RecyclerView addressRecycleView;
    private AddressDeliveryAdapter addressDeliveryAdapter;
    private List<AddressDeliveryDTO> addressDeliveryDTOList;
    ArrayList<CardItemDTO> selectedItems;
    private ImageButton backIcon;

    private final long userId = SharedPrefManager.getUserId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_delivery_layout);
        addressRecycleView = findViewById(R.id.addressRecyclerView);
        Button addNewAddress = findViewById(R.id.addNewAddressButton);
        backIcon = findViewById(R.id.backIcon);
        addressRecycleView.setLayoutManager(new LinearLayoutManager(this));

        addressDeliveryDTOList = new ArrayList<>();
        getAllAddressDelivery(userId);

        selectedItems = getIntent().getParcelableArrayListExtra("selectedItems"); // Lưu lại để dùng khi back
        addNewAddress.setOnClickListener(v -> {
            Intent intent = new Intent(AddressDeliveryActivity.this, AddAddressDeliveryActivity.class);
            intent.putParcelableArrayListExtra("selectedItems", selectedItems);
            startActivity(intent);
            finish();
        });

        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(AddressDeliveryActivity.this, PaymentActivity.class);
            // Lấy danh sách sản phẩm mẫu
            intent.putParcelableArrayListExtra("selectedItems", selectedItems);
            startActivity(intent);
            finish();
        });
    }

    private void getAllAddressDelivery(long userId) {
        IAddressDelivery apiService = APIRetrofit.getRetrofitInstance().create(IAddressDelivery.class);
        Call<List<AddressDeliveryDTO>> call = apiService.getAllAddressByUserId(userId);
        call.enqueue(new Callback<List<AddressDeliveryDTO>>() {
            @Override
            public void onResponse(Call<List<AddressDeliveryDTO>> call, Response<List<AddressDeliveryDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    addressDeliveryDTOList = response.body();
                    addressDeliveryAdapter = new AddressDeliveryAdapter(addressDeliveryDTOList, AddressDeliveryActivity.this);
                    addressRecycleView.setAdapter(addressDeliveryAdapter);
                } else {
                    Toast.makeText(AddressDeliveryActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                    Log.e("AddressDeliveryActivity", "API Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<AddressDeliveryDTO>> call, Throwable t) {
                Toast.makeText(AddressDeliveryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddressDeliveryActivity", "API Call Failure: " + t.getMessage());
            }
        });
    }

    public void updateDefaultAddress(long userId, long addressId) {
        // Call API to update the address as default
        IAddressDelivery apiService = APIRetrofit.getRetrofitInstance().create(IAddressDelivery.class);
        Call<String> call = apiService.setDefaultAddress(userId, addressId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    Toast.makeText(AddressDeliveryActivity.this, "Address updated successfully: " + response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error
                    Toast.makeText(AddressDeliveryActivity.this, "Failed to update address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure
                Log.e("AddressDeliveryActivity", "Error updating address: " + t.getMessage());
                Toast.makeText(AddressDeliveryActivity.this, "Error updating address", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
