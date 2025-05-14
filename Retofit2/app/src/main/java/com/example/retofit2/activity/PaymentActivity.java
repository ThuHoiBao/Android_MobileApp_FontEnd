package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.ProductPaymentAdapter;
import com.example.retofit2.api.IAddressDelivery;
import com.example.retofit2.api.IOrderAPI;
import com.example.retofit2.api.VNPayAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.CreateOrderRequestDTO;
import com.example.retofit2.dto.requestDTO.PaymentRequestDTO;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.responseDTO.OrderResponseDTO;
import com.example.retofit2.utils.OrderCallback;
import com.example.retofit2.utils.SharedPrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView productsRecycleView;
    private ProductPaymentAdapter productPaymentAdapter;

    private List<CardItemDTO> selectedItems;
    private List<CardItemDTO> loadOrderItems;
    final long userId = SharedPrefManager.getUserId();
    private TextView fullNameText, phoneNumberText, addressText;
    private TextView totalAmountText, discountAmountText;
    private long totalAmount;
    private ImageButton backIcon, nextButton;
    private Button placeOrderButton;
    private int orderId;

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
        placeOrderButton = findViewById(R.id.placeOrderButton);
        totalAmountText = findViewById(R.id.totalAmountText);

        // Lấy danh sách sản phẩm mẫu
        Intent intent1 = getIntent();
        selectedItems = intent1.getParcelableArrayListExtra("selectedItems");
        if (selectedItems != null) {
            productPaymentAdapter = new ProductPaymentAdapter(selectedItems);
            productsRecycleView.setAdapter(productPaymentAdapter);
            updateTotalAmount();
        } else {
            Log.e("PaymentActivity", "No items selected");
             orderId = intent1.getIntExtra("orderId", -1);
            if(orderId != -1){
                callLoadOrderItems(orderId);
            } else {
                Toast.makeText(this, "No order ID received", Toast.LENGTH_SHORT).show();
            }

        }

        backIcon.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        });

        //Lay thong tin thanh toans
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, AddressDeliveryActivity.class);
            intent.putParcelableArrayListExtra("selectedItems", new ArrayList<>(selectedItems));
            startActivity(intent);
            finish();
        });

        getDefaultDeliveryAddress(userId);

        placeOrderButton.setOnClickListener(v -> {
            if(selectedItems != null){
                CreateOrderRequestDTO createOrderRequestDTO = new CreateOrderRequestDTO();
                createOrderRequestDTO.setUserId(SharedPrefManager.getUserId());
                List<Integer> list= new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++){
                    list.add(selectedItems.get(i).getCartItemId());
                }
                createOrderRequestDTO.setCartItemIds(list);
                createOrder(createOrderRequestDTO, new OrderCallback() {
                    @Override
                    public void onSuccess(OrderResponseDTO orderResponseDTO) {
                        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(orderResponseDTO.getOrderId(), totalAmount, "NCB", "vn");
                        createPaymentUrl(paymentRequestDTO);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getApplicationContext(), "Lỗi khi tạo Order: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                if(orderId != -1){
                    PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(orderId, totalAmount, "NCB", "vn");
                    createPaymentUrl((paymentRequestDTO));
                }
            }
        });
    }

    private void callLoadOrderItems(int orderId) {
        IOrderAPI orderAPI = APIRetrofit.getOrderApiService();
        orderAPI.getOrderItems(orderId).enqueue(new Callback<List<CardItemDTO>>() {
            @Override
            public void onResponse(Call<List<CardItemDTO>> call, Response<List<CardItemDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadOrderItems = response.body();
                    // Cập nhật giao diện UI sau khi lấy dữ liệu thành công
                    productPaymentAdapter = new ProductPaymentAdapter(loadOrderItems);
                    productsRecycleView.setAdapter(productPaymentAdapter);
                    updateTotalAmount(); // Cập nhật lại tổng tiền
                } else {
                    Toast.makeText(PaymentActivity.this, "Error: Unable to load order items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CardItemDTO>> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createOrder(CreateOrderRequestDTO requestDTO, OrderCallback callback) {
        IOrderAPI orderAPI = APIRetrofit.getOrderApiService();
        orderAPI.createOrderFromCart(requestDTO).enqueue(new Callback<OrderResponseDTO>() {
            @Override
            public void onResponse(Call<OrderResponseDTO> call, Response<OrderResponseDTO> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Lỗi khi tạo Order: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<OrderResponseDTO> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



    private void createPaymentUrl(PaymentRequestDTO paymentRequestDTO) {
        VNPayAPI vnPayAPI = APIRetrofit.getVNPAYApiService();
        vnPayAPI.createPaymentUrl(paymentRequestDTO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String paymentUrl = response.body();
                    Log.d("PaymentActivity", "Payment URL: " + paymentUrl);

                    Intent intent = new Intent(PaymentActivity.this, PaymentWebViewActivity.class);
                    intent.putExtra("paymentUrl", paymentUrl);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi tạo URL thanh toán", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi khi tạo URL thanh toán", Toast.LENGTH_LONG).show();
            }
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


    private void updateTotalAmount() {
        totalAmount = 0;
        if(selectedItems != null){
            for (CardItemDTO item : selectedItems) {
                if (!item.isOutOfStockItems()) { // Không cần kiểm tra isSelected() nếu đã lọc trước đó
                    totalAmount += item.getPrice() * item.getQuantity();
                }
            }
        } else {
            for(CardItemDTO item : loadOrderItems){
                if(!item.isOutOfStockItems()){
                    totalAmount += item.getPrice()  * item.getQuantity();
                }
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(totalAmount);
        totalAmountText.setText("Tổng tiền: " + formattedPrice + "đ");
    }
}
