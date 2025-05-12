package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retofit2.R;
import com.example.retofit2.api.IAddressDelivery;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.AddressDeliveyRequestDTO;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;
import com.example.retofit2.utils.SharedPrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressDeliveryActivity extends AppCompatActivity {
    private EditText fullNameEditText, phoneNumberEditText, addressEditText;
    private Button btnAddAddress;
    final long userId = SharedPrefManager.getUserId();
    ArrayList<CardItemDTO> selectedItems;
    ImageButton backIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_address);
        fullNameEditText = findViewById(R.id.fullNameText);
        phoneNumberEditText = findViewById(R.id.phoneNumberText);
        addressEditText = findViewById(R.id.addressText);
        btnAddAddress = findViewById(R.id.addNewAddressDelivery);
        backIcon = findViewById(R.id.backIcon);

        btnAddAddress.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                Toast.makeText(AddAddressDeliveryActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Tạo đối tượng DTO với dữ liệu người dùng nhập
                AddressDeliveyRequestDTO addressDTO = new AddressDeliveyRequestDTO();
                addressDTO.setFullName(fullName);
                addressDTO.setPhoneNumber(phoneNumber);
                addressDTO.setAddress(address);
                addressDTO.setUserId(userId);
                addressDTO.setDefault(false);

                // Gọi API để thêm địa chỉ mới
                addNewAddress(addressDTO);
            }
        });

        selectedItems = getIntent().getParcelableArrayListExtra("selectedItems"); // Lưu lại để dùng khi back
        backIcon.setOnClickListener(v ->  {
            Intent intent = new Intent(AddAddressDeliveryActivity.this, AddressDeliveryActivity.class);
            intent.putParcelableArrayListExtra("selectedItems", selectedItems);
            startActivity(intent);
            finish();
        });
    }

    private void addNewAddress(AddressDeliveyRequestDTO addressDTO) {
        IAddressDelivery apiService = APIRetrofit.getRetrofitInstance().create(IAddressDelivery.class);
        Call<AddressDeliveryDTO> call = apiService.createAddress(addressDTO);
        call.enqueue(new Callback<AddressDeliveryDTO>() {
            @Override
            public void onResponse(Call<AddressDeliveryDTO> call, Response<AddressDeliveryDTO> response) {
                if(response.isSuccessful() && response.body() != null){
                    AddressDeliveryDTO saveAddress = response.body();
                    Toast.makeText(AddAddressDeliveryActivity.this, "Địa chỉ đã được thêm: " + saveAddress.getFullName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddAddressDeliveryActivity.this, "Thêm địa chỉ không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddressDeliveryDTO> call, Throwable t) {
                Toast.makeText(AddAddressDeliveryActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
