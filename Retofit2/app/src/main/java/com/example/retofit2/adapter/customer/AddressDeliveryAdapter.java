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
import com.example.retofit2.api.IAddressDelivery;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.responseDTO.AddressDeliveryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // Set address info
        holder.addressName.setText(address.getFullName());
        holder.addPhone.setText(address.getPhoneNumber());
        holder.addressDetails.setText(address.getAddress());

        // Set radio button
        holder.selectAddressRadioButton.setChecked(position == selectedPosition);
        holder.defaultLabel.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

        // Handle radio button click
        holder.selectAddressRadioButton.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            selectedPosition = currentPos;

            AddressDeliveryDTO selectedAddress = addressList.get(currentPos);
            if (activity != null) {
                activity.updateDefaultAddress(selectedAddress.getUserId(), selectedAddress.getId());
            }

            notifyDataSetChanged();
        });

        // Handle delete icon click
        holder.deleteAddressIcon.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            AddressDeliveryDTO toDelete = addressList.get(currentPos);

            IAddressDelivery apiService = APIRetrofit.getAddressDelivery();
            apiService.deleteAddress(toDelete.getId(), toDelete.getUserId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        addressList.remove(currentPos);
                        notifyItemRemoved(currentPos);

                        if (selectedPosition == currentPos) {
                            selectedPosition = -1;
                        } else if (selectedPosition > currentPos) {
                            selectedPosition--;
                        }

                        Log.d("AddressAdapter", "Address deleted successfully.");
                    } else {
                        Log.e("AddressAdapter", "Failed to delete address.");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("AddressAdapter", "Error deleting address: " + t.getMessage());
                }
            });
        });
    }
    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {

        RadioButton selectAddressRadioButton;
        TextView addressName, addPhone, addressDetails, defaultLabel;
        ImageView deleteAddressIcon;

        public AddressViewHolder(View itemView) {
            super(itemView);
            selectAddressRadioButton = itemView.findViewById(R.id.selectAddressRadioButton);
            addressName = itemView.findViewById(R.id.addressName);
            addPhone = itemView.findViewById(R.id.phoneNumber);
            addressDetails = itemView.findViewById(R.id.addressDetails);
            defaultLabel = itemView.findViewById(R.id.defaultLabel);
            deleteAddressIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
