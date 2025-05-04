package com.example.retofit2.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.ProductPaymentAdapter;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.requestDTO.PaymentInfo;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView productsRecycleView;
    private ProductPaymentAdapter productPaymentAdapter;
    private List<CardItemDTO> productList;

    private TextView totalAmountText, discountAmountText, addressText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        productsRecycleView = findViewById(R.id.productRecyclerView);
        productsRecycleView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách sản phẩm mẫu
        productList = getProductList(); // Lấy danh sách sản phẩm
        productPaymentAdapter = new ProductPaymentAdapter(productList);
        productsRecycleView.setAdapter(productPaymentAdapter);

        // Khởi tạo thông tin thanh toán
        PaymentInfo paymentInfo = new PaymentInfo(
                "Vương Đức Thoại (+84) 859 716 818\nKí Túc Xá D2 spkt, 484a Đường Lê Văn Việt, Phường Tăng Nhơn Phú A, TP. Hồ Chí Minh",
                "₫25.000"
        );

        // Gán dữ liệu vào các view
        totalAmountText = findViewById(R.id.totalAmountText);
        addressText = findViewById(R.id.addressText);

        totalAmountText.setText(paymentInfo.getTotalAmount());
        addressText.setText(paymentInfo.getAddress());
    }


    public List<CardItemDTO> getProductList() {
        List<CardItemDTO> productList = new ArrayList<>();

        CardItemDTO cardItemDTO1 = new CardItemDTO("Đồng hồ nam Led LP22",
                23000,
                1);
//        cardItemDTO1.setImageUrl("https://cdn.tgdd.vn/Products/Images/42/281570/iphone-15-hong-thumb-1-600x600.jpg");

        CardItemDTO cardItemDTO2 = new CardItemDTO("Đồng hồ nam Led LP22",
                23000,
                1);
//        cardItemDTO1.setImageUrl("https://cdn.tgdd.vn/Products/Images/42/281570/iphone-15-hong-thumb-1-600x600.jpg");

        productList.add(cardItemDTO1);


        productList.add(cardItemDTO2);
        return productList;
    }
}
