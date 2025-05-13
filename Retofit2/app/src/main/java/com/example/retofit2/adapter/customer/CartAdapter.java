package com.example.retofit2.adapter.customer;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.retofit2.R;
import com.example.retofit2.api.ICartAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.CardItemDTO;
import com.example.retofit2.dto.requestDTO.CartItemUpdateRequestDTO;
import com.example.retofit2.dto.responseDTO.CartItemUpdateResultDTO;
import com.example.retofit2.dto.responseDTO.ResponseObject;
import com.example.retofit2.utils.SharedPrefManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// ... (imports như cũ)
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CardItemDTO> cartItems;
    private double totalAmount = 0.0;
    private RecyclerView recyclerView;
    private TextView totalAmountText;


    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateRunnable = this::sendBatchUpdate;
    private final long DEBOUNCE_DELAY = 800;
    private final Map<String, CardItemDTO> modifiedItems = new HashMap<>();
    String token = SharedPrefManager.getToken();
    public CartAdapter(List<CardItemDTO> cartItems, RecyclerView recyclerView, TextView totalAmountText) {
        this.cartItems = cartItems;
        this.recyclerView = recyclerView;
        this.totalAmountText = totalAmountText;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CardItemDTO cardItemDTO = cartItems.get(position);
        holder.productName.setText(cardItemDTO.getProductName());
        holder.color.setText(cardItemDTO.getColor());

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(cardItemDTO.getPrice());
        holder.productPrice.setText(formattedPrice + "đ");



        // Gỡ TextWatcher cũ nếu có
        if (holder.quantityWatcher != null) {
            holder.quantity.removeTextChangedListener(holder.quantityWatcher);
        }

        holder.quantity.setText(String.valueOf(cardItemDTO.getQuantity()));

        // Tải ảnh sản phẩm
        String imageUrl = cardItemDTO.getProductImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.iphone3);
        }

        holder.checkBox.setChecked(cardItemDTO.isSelected());

        if(cardItemDTO.isOutOfStockItems()){
            holder.checkBox.setEnabled(false);
            holder.itemView.setAlpha(0.7f);
            holder.increaseButton.setEnabled(false);
            holder.deleteButton.setEnabled(true);
            holder.quantity.setEnabled(false);
            holder.message.setText(cardItemDTO.getMessage());
        } else {
            holder.checkBox.setEnabled(true);
            holder.itemView.setAlpha(1.0f);
            holder.increaseButton.setEnabled(true);
            holder.decreaseButton.setEnabled(true);
            holder.quantity.setEnabled(true);
            holder.message.setText(cardItemDTO.getMessage());
        }

        // Nút tăng
        holder.increaseButton.setOnClickListener(v -> {
            int currentQty = cardItemDTO.getQuantity();
            if (currentQty < 10) {
                int newQty = currentQty + 1;
                cardItemDTO.setQuantity(newQty);
                if (!holder.quantity.getText().toString().equals(String.valueOf(newQty))) {
                    holder.quantity.setText(String.valueOf(newQty));
                }
                trackItemChange(cardItemDTO);
                updateTotalAmount();
            }
        });

        // Nút giảm
        holder.decreaseButton.setOnClickListener(v -> {
            int currentQty = cardItemDTO.getQuantity();
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                cardItemDTO.setQuantity(newQty);
                if (!holder.quantity.getText().toString().equals(String.valueOf(newQty))) {
                    holder.quantity.setText(String.valueOf(newQty));
                }
                trackItemChange(cardItemDTO);
                updateTotalAmount();
            }
        });

        // CheckBox chọn sản phẩm
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cardItemDTO.setSelected(isChecked);
            updateTotalAmount();
        });

        // Cập nhật TextWatcher mới
        holder.quantityWatcher = new TextWatcher() {
            private boolean isEditing = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;

                isEditing = true;
                try {
                    String input = s.toString().trim();
                    if (!input.isEmpty()) {
                        int newQty = Integer.parseInt(input);
                        if (newQty < 1) newQty = 1;
                        if (newQty > 10) newQty = 10;

                        if (cardItemDTO.getQuantity() != newQty) {
                            cardItemDTO.setQuantity(newQty);
                            trackItemChange(cardItemDTO);
                            updateTotalAmount();
                        }

                        if (!input.equals(String.valueOf(newQty))) {
                            holder.quantity.setText(String.valueOf(newQty));
                            holder.quantity.setSelection(holder.quantity.getText().length());
                        }
                    }
                } catch (NumberFormatException e) {
                    holder.quantity.setText("1");
                    holder.quantity.setSelection(1);
                    cardItemDTO.setQuantity(1);
                    trackItemChange(cardItemDTO);
                    updateTotalAmount();
                }
                isEditing = false;
            }
        };

        holder.quantity.addTextChangedListener(holder.quantityWatcher);

        holder.deleteButton.setOnClickListener(v -> {
            int cardItemId = cardItemDTO.getCartItemId();
            deleteCartItem(cardItemId);
        });
    }

    private void deleteCartItem(int cardItemId) {
        ICartAPI apiService = APIRetrofit.getCartAPIService(token);
        apiService.removeCartItem(cardItemId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    Log.d("CartAdapter", "Item deleted successfully. Server response: " + result);

                    cartItems.removeIf(item -> item.getCartItemId() == cardItemId);
                    notifyDataSetChanged();
                    updateTotalAmount();
                } else {
                    Log.e("CartAdapter", "Failed to delete item.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("CartAdapter", "Error deleting item: " + t.getMessage());
            }
        });
    }

    private void trackItemChange(CardItemDTO item) {
        String key = item.getProductName() + "_" + item.getColor();
        modifiedItems.put(key, item);
        handler.removeCallbacks(updateRunnable);
        handler.postDelayed(updateRunnable, DEBOUNCE_DELAY);
    }

    private void sendBatchUpdate() {
        if (modifiedItems.isEmpty()) return;

        List<CartItemUpdateRequestDTO> updates = new ArrayList<>();
        for (CardItemDTO item : modifiedItems.values()) {
            updates.add(new CartItemUpdateRequestDTO(item.getCartItemId(),item.getProductName(), item.getColor(), item.getQuantity()));
        }

        ICartAPI apiService = APIRetrofit.getCartAPIService(token);

        apiService.updateCartItems(updates).enqueue(new Callback<List<CartItemUpdateResultDTO>>() {
            @Override
            public void onResponse(Call<List<CartItemUpdateResultDTO>> call, Response<List<CartItemUpdateResultDTO>> response) {
                if (response.isSuccessful()) {
                    List<CartItemUpdateResultDTO>  result = response.body();
                    updateTotalAmount();
                    Log.d("CartAdapter", "Batch update successful.");
                    for (int i = 0; i < result.size(); i ++) {
                        CartItemUpdateResultDTO itemResult = result.get(i);
                        CardItemDTO item = cartItems.get(i);// Lấy item từ cartItems theo vị trí
                        if (itemResult.isOutOfStock()) {
                            item.setOutOfStockItems(true);
//                            item.setQuantity(itemResult.getUpdatedQuantity());
                            item.setMessage(itemResult.getMessage());
                        } else {
                            item.setOutOfStockItems(false);
                            item.setQuantity(itemResult.getUpdatedQuantity());
                            item.setMessage(itemResult.getMessage());
                        }
                        notifyItemChanged(i);
                    }

                    modifiedItems.clear();
                } else {
                    Log.e("CartAdapter", "Batch update failed.");
                }
            }

            @Override
            public void onFailure(Call<List<CartItemUpdateResultDTO>> call, Throwable t) {
                Log.e("CartAdapter", "Batch update error: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateTotalAmount() {
        totalAmount = 0.0;
        for (CardItemDTO item : cartItems) {
            if (item.isSelected() && !item.isOutOfStockItems()) {
                totalAmount += item.getPrice() * item.getQuantity();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedPrice = decimalFormat.format(totalAmount);
        totalAmountText.setText("Total Amount: " + formattedPrice + "đ");

        recyclerView.post(() -> notifyDataSetChanged());
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, color, message;
        EditText quantity;
        ImageView productImage;
        CheckBox checkBox;
        ImageButton increaseButton, decreaseButton, deleteButton;
        TextWatcher quantityWatcher;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            color = itemView.findViewById(R.id.productColor);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            productImage = itemView.findViewById(R.id.productImage);
            checkBox = itemView.findViewById(R.id.selectProductCheckbox);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            message = itemView.findViewById(R.id.message);
        }
    }
}
