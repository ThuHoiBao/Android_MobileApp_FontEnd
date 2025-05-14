package com.example.retofit2.adapter.customer;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CardItemDTO> cartItems;
    private double totalAmount = 0.0;
    private RecyclerView recyclerView;
    private TextView totalAmountText;
    private OnCartItemClickListener listener;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateRunnable = this::sendBatchUpdate;
    private final long DEBOUNCE_DELAY = 800;
    private final Map<String, CardItemDTO> modifiedItems = new HashMap<>();
    private final String token = SharedPrefManager.getToken();

    public CartAdapter(List<CardItemDTO> cartItems, RecyclerView recyclerView, TextView totalAmountText, OnCartItemClickListener listener) {
        this.cartItems = cartItems;
        this.recyclerView = recyclerView;
        this.totalAmountText = totalAmountText;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CardItemDTO item = cartItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.color.setText(item.getColor());
        holder.productPrice.setText(new DecimalFormat("#,###").format(item.getPrice()) + "đ");
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
            Picasso.get().load(item.getProductImage()).into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.iphone3);
        }

        // Gỡ và gán lại checkbox listener
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setOnCheckedChangeListener((btn, isChecked) -> {
            item.setSelected(isChecked);
            updateTotalAmount();
        });

        if (item.isOutOfStockItems()) {
            holder.checkBox.setEnabled(false);
            holder.itemView.setAlpha(0.7f);
            holder.increaseButton.setEnabled(false);
            holder.decreaseButton.setEnabled(false);
            holder.quantity.setEnabled(false);
            String message = item.getMessage();
            holder.message.setText(message);
            if (message != null && !message.isEmpty()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        CardItemDTO currentItem = cartItems.get(currentPosition);
                        if (message.equals(currentItem.getMessage())) {
                            currentItem.setMessage("");
                            holder.message.setText("");
                        }
                    }
                }, 3000);
            }

        } else {
            holder.checkBox.setEnabled(true);
            holder.itemView.setAlpha(1.0f);
            holder.increaseButton.setEnabled(true);
            holder.decreaseButton.setEnabled(true);
            holder.quantity.setEnabled(true);
            String message = item.getMessage();
            holder.message.setText(message);
            if (message != null && !message.isEmpty()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        CardItemDTO currentItem = cartItems.get(currentPosition);
                        if (message.equals(currentItem.getMessage())) {
                            currentItem.setMessage("");
                            holder.message.setText("");
                        }
                    }
                }, 3000);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item); // Gửi sản phẩm được click
            }
        });

        // Xử lý nút tăng
        holder.increaseButton.setOnClickListener(v -> {
            int qty = item.getQuantity();
            if (qty < 10) {
                item.setQuantity(qty + 1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                trackItemChange(item);
                updateTotalAmount();
            }
        });

        // Xử lý nút giảm
        holder.decreaseButton.setOnClickListener(v -> {
            int qty = item.getQuantity();
            if (qty > 1) {
                item.setQuantity(qty - 1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
                trackItemChange(item);
                updateTotalAmount();
            }
        });

        // Gỡ watcher cũ trước khi gán mới
        if (holder.quantityWatcher != null) {
            holder.quantity.removeTextChangedListener(holder.quantityWatcher);
        }

        holder.quantityWatcher = new TextWatcher() {
            private boolean editing = false;

            @Override
            public void afterTextChanged(Editable s) {
                if (editing) return;
                editing = true;
                try {
                    int newQty = Integer.parseInt(s.toString().trim());
                    newQty = Math.max(1, Math.min(newQty, 10));
                    if (newQty != item.getQuantity()) {
                        item.setQuantity(newQty);
                        trackItemChange(item);
                        updateTotalAmount();
                    }
                    if (!s.toString().equals(String.valueOf(newQty))) {
                        holder.quantity.setText(String.valueOf(newQty));
                        holder.quantity.setSelection(holder.quantity.getText().length());
                    }
                } catch (Exception e) {
                    holder.quantity.setText("1");
                    holder.quantity.setSelection(1);
                    item.setQuantity(1);
                    trackItemChange(item);
                    updateTotalAmount();
                }
                editing = false;
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        holder.quantity.addTextChangedListener(holder.quantityWatcher);

        // Xử lý nút xóa
        holder.deleteButton.setOnClickListener(v -> deleteCartItem(item.getCartItemId()));
    }

    private void deleteCartItem(int cartItemId) {
        APIRetrofit.getCartAPIService(token).removeCartItem(cartItemId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    cartItems.removeIf(item -> item.getCartItemId() == cartItemId);
                    notifyDataSetChanged();
                    updateTotalAmount();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("CartAdapter", "Delete error: " + t.getMessage());
            }
        });
    }

    private void trackItemChange(CardItemDTO item) {
        String key = item.getCartItemId() + "";
        modifiedItems.put(key, item);
        handler.removeCallbacks(updateRunnable);
        handler.postDelayed(updateRunnable, DEBOUNCE_DELAY);
    }

    private void sendBatchUpdate() {
        if (modifiedItems.isEmpty()) return;

        List<CartItemUpdateRequestDTO> updates = new ArrayList<>();
        for (CardItemDTO item : modifiedItems.values()) {
            updates.add(new CartItemUpdateRequestDTO(item.getCartItemId(), item.getProductName(), item.getColor(), item.getQuantity()));
        }

        APIRetrofit.getCartAPIService(token).updateCartItems(updates).enqueue(new Callback<List<CartItemUpdateResultDTO>>() {
            @Override
            public void onResponse(Call<List<CartItemUpdateResultDTO>> call, Response<List<CartItemUpdateResultDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (CartItemUpdateResultDTO result : response.body()) {
                        for (int i = 0; i < cartItems.size(); i++) {
                            CardItemDTO item = cartItems.get(i);
                            if (item.getCartItemId() == result.getCartItemId()) {
                                item.setQuantity(result.getUpdatedQuantity());
                                item.setOutOfStockItems(result.isOutOfStock());
                                item.setMessage(result.getMessage());
                                notifyItemChanged(i);
                                break;
                            }
                        }
                    }
                    modifiedItems.clear();
                    updateTotalAmount();
                }
            }

            @Override
            public void onFailure(Call<List<CartItemUpdateResultDTO>> call, Throwable t) {
                Log.e("CartAdapter", "Batch update error: " + t.getMessage());
            }
        });
    }

    public void updateTotalAmount() {
        totalAmount = 0.0;
        for (CardItemDTO item : cartItems) {
            if (item.isSelected() && !item.isOutOfStockItems()) {
                totalAmount += item.getPrice() * item.getQuantity();
            }
        }
        totalAmountText.setText("Total Amount: " + new DecimalFormat("#,###").format(totalAmount) + "đ");
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
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
            productPrice = itemView.findViewById(R.id.productPrice);
            color = itemView.findViewById(R.id.productColor);
            quantity = itemView.findViewById(R.id.quantity);
            productImage = itemView.findViewById(R.id.productImage);
            checkBox = itemView.findViewById(R.id.selectProductCheckbox);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            message = itemView.findViewById(R.id.message);
        }
    }

    public interface OnCartItemClickListener {
        void onItemClick(CardItemDTO item);
    }
}
