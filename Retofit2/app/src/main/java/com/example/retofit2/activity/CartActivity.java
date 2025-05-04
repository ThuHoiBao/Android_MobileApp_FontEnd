package com.example.retofit2.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.CartAdapter;
import com.example.retofit2.dto.requestDTO.CardItemDTO;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CardItemDTO> cartItems;
    private TextView totalAmountText;
    private AppCompatButton shippingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountText = findViewById(R.id.totalAmountText);
        shippingButton = findViewById(R.id.shippingButton);

        cartItems = new ArrayList<>();
        cartItems.add(new CardItemDTO("Mango Juice Natural Fruits", 4.00, 1));
        cartItems.add(new CardItemDTO("Healthy Concord Grape Juice", 4.00, 3));
        cartItems.add(new CardItemDTO("Orange Juice Nutrition Facts", 6.00, 1));
        cartItems.add(new CardItemDTO("Red Tomato Hybrid Vegetable Seeds", 3.00, 1));

        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Update total amount (assuming no discount for now)
        double totalAmount = 0.0;
        for (CardItemDTO item : cartItems) {
            totalAmount += item.getPrice() * item.getQuantity();
        }
        totalAmountText.setText("Total Amount: $" + totalAmount);

        shippingButton.setOnClickListener(v -> {
            // Handle shipping action
            Toast.makeText(CartActivity.this, "Proceeding to Shipping", Toast.LENGTH_SHORT).show();
        });
    }

//    public String

}