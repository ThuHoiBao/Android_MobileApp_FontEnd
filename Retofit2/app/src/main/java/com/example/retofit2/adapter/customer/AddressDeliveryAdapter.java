package com.example.retofit2.adapter.customer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retofit2.R;
import com.example.retofit2.activity.AddressDeliveryActivity;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;

import java.util.List;

public class AddressDeliveryAdapter extends RecyclerView.Adapter<AddressDeliveryAdapter.AddressViewHolder> {
    private List<AddressDeliveryDTO> addressList;
    private int selectedPosition = -1;  // To track the selected address
    private AddressDeliveryActivity activity;

    public AddressDeliveryAdapter(List<AddressDeliveryDTO> addressList, AddressDeliveryActivity activity) {
        this.addressList = addressList;
        this.activity = activity;
        for (int i = 0; i < addressList.size(); i++) {
            // Find the default address and set it as selected
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
        AddressDeliveryDTO address = addressList.get(position);

        // Set the address information
        holder.addressName.setText(address.getFullName());
        holder.addPhone.setText(address.getPhoneNumber());
        Log.d("phone", "Extracted Address Id: " + address.getPhoneNumber());
        holder.addressDetails.setText(address.getAddress());

        // Handle the selected radio button
        holder.selectAddressRadioButton.setChecked(position == selectedPosition);

        // Show label "Default" if it's the default address
        if (position == selectedPosition) {
            holder.defaultLabel.setVisibility(View.VISIBLE); // Show label
        } else {
            holder.defaultLabel.setVisibility(View.GONE); // Hide label if not default
        }

        // Handle click event on the radio button
        holder.selectAddressRadioButton.setOnClickListener(v -> {
            // Update selected position
            selectedPosition = holder.getAdapterPosition();

            // Call API to update the address as default
            if (activity != null) {
                activity.updateDefaultAddress(address.getUserId(), address.getId());  // Call API to update the address
            }

            // Notify RecyclerView to refresh the list
            notifyDataSetChanged();
        });

        // Optionally handle clicks on the edit icon
        holder.editAddressIcon.setOnClickListener(v -> {
            // Handle address edit
            // You can open an edit address activity or dialog here
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
