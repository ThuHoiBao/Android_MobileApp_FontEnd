package com.example.retofit2.adapter.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.dto.requestDTO.AddressDelivery;

import java.util.List;

public class AddressDeliveryAdapter extends RecyclerView.Adapter<AddressDeliveryAdapter.AddressViewHolder> {
    private List<AddressDelivery> addressList;
    private int selectedPosition = -1;

    public AddressDeliveryAdapter(List<AddressDelivery> addressList) {
        this.addressList = addressList;
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).isDefault()) {
                selectedPosition = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_delivery_item, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressDelivery address = addressList.get(position);

        holder.addressName.setText(address.getName());
        holder.addPhone.setText(address.getPhone());
        holder.addressDetails.setText(address.getDetails());

        // Kiểm tra xem địa chỉ hiện tại có phải là mặc định không
        holder.selectAddressRadioButton.setChecked(position == selectedPosition); // Chỉ đánh dấu nếu đây là địa chỉ được chọn

        // Hiển thị label "Mặc định" nếu địa chỉ là mặc định
        if (position == selectedPosition) {
            holder.defaultLabel.setVisibility(View.VISIBLE); // Hiển thị label
        } else {
            holder.defaultLabel.setVisibility(View.GONE); // Ẩn label nếu không phải mặc định
        }

        // Xử lý click trên radio button
        holder.selectAddressRadioButton.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition(); // Cập nhật địa chỉ mặc định
            notifyDataSetChanged(); // Cập nhật lại RecyclerView
        });


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {

        RadioButton selectAddressRadioButton;
        TextView addressName, addPhone, addressDetails, defaultLabel;
        ImageView editAddressIcon;

        public AddressViewHolder(View itemView) {
            super(itemView);
            selectAddressRadioButton = itemView.findViewById(R.id.selectAddressRadioButton);
            addressName = itemView.findViewById(R.id.addressName);
            addPhone = itemView.findViewById(R.id.phoneNumber);
            addressDetails = itemView.findViewById(R.id.addressDetails);
            defaultLabel = itemView.findViewById(R.id.defaultLabel);
            editAddressIcon = itemView.findViewById(R.id.editAddressIcon);
        }
    }
}
