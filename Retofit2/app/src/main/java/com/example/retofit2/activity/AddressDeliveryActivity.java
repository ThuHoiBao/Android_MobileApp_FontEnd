package com.example.retofit2.activity;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.AddressDeliveryAdapter;
import com.example.retofit2.dto.requestDTO.AddressDelivery;

import java.util.ArrayList;
import java.util.List;

public class AddressDeliveryActivity extends AppCompatActivity {
    private RecyclerView addressRecycleView;
    private AddressDeliveryAdapter addressDeliveryAdapter;
    private List<AddressDelivery> addressDeliveryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_delivery_layout);
        addressRecycleView = findViewById(R.id.addressRecyclerView);
        addressRecycleView.setLayoutManager(new LinearLayoutManager(this));

        addressDeliveryList = new ArrayList<>();
        addressDeliveryList.add(new AddressDelivery("Vương Đức Thoại", "0859716818" ,"Kí Túc Xá D2 spkt, 484a Đường Lê Văn Việt, Phường Tăng Nhơn Phú A, TP. Hồ Chí Minh", true));
        addressDeliveryList.add(new AddressDelivery("Vương Đức Thoại", "0859716818", "Mỹ Lệ Tây, Trụ Sở Khu Phố Mỹ Lệ Tây, Thị trấn Phú Thứ, Huyện Tây Hòa, Phú Yên", false));
        addressDeliveryAdapter = new AddressDeliveryAdapter(addressDeliveryList);
        addressRecycleView.setAdapter(addressDeliveryAdapter);
    }
}
