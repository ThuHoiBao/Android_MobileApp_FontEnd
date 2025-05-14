package com.example.retofit2.adapter.customer;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retofit2.R;
import com.example.retofit2.activity.FeedBackActivity;
import com.example.retofit2.activity.PaymentActivity;
import com.example.retofit2.activity.ProductDetailActivity;
import com.example.retofit2.api.OrderItemAPI;
import com.example.retofit2.api.ReviewAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.OrderItemRequestDTO;
import com.example.retofit2.dto.requestDTO.ReviewRequestDTO;
import com.example.retofit2.dto.responseDTO.ReviewResponseDTO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat; // Import SimpleDateFormat
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderProductViewHolder> {

    private List<OrderItemRequestDTO> orderList;
    private Context context;

    public OrderItemAdapter(Context context, List<OrderItemRequestDTO> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public OrderProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_order_item_home, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderProductViewHolder holder, int position) {
        OrderItemRequestDTO order = orderList.get(position);

        // Set dữ liệu
        holder.productName.setText(order.getProductName());
        holder.productColor.setText("Màu sắc: " + order.getColor());

        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Define the decimal format
        String formattedProductPrice = decimalFormat.format(order.getPrice()); // Format the product price
        holder.productPrice.setText(formattedProductPrice + "₫"); // Set the formatted price to the view

        String formattedTotalPrice = decimalFormat.format(order.getTotalPrice()); // Format the product price
        holder.productTotal.setText("Tổng: " + formattedTotalPrice+"₫");
        holder.productQuantity.setText("x"+order.getQuantity());


        // Định dạng ngày tháng năm
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng: ngày/tháng/năm
        String formattedDate = dateFormat.format(order.getOrderDate());
        String orderStatus="";
        String orderStatusing="";
        // Trạng thái đơn hàng
        if (order.getOrderStatus().equals("COMPLETED")) {
            orderStatus="Đã giao hàng.   ĐH: " + order.getOrderId();
            orderStatusing="Đơn hàng của bạn đã được giao thành công";
        } else if (order.getOrderStatus().equals("SHIPPED")) {
            orderStatus="Đang vận.   ĐH: "+ order.getOrderId();
            orderStatusing="Đơn hàng của bạn đang được vận chuyển";
        }  else if(order.getOrderStatus().equals("CANCELLED")){
            orderStatus="Đã hủy.   ĐH: "+ order.getOrderId();
            orderStatusing="Đơn hàng của bạn đã được hủy";
        }
        else if(order.getOrderStatus().equals("FEEDBACKED")){
            orderStatus="Đã đánh giá.   ĐH: "+ order.getOrderId();
            orderStatusing="Kiện hàng của bạn đã được đánh giá";
        }
        else if(order.getOrderStatus().equals("PENDING")){
            orderStatus="Chưa thanh toán.   ĐH: "+ order.getOrderId();
            orderStatusing="Đơn hàng của bạn chưa được thanh toán";
        }
        else {
            orderStatus="Đã đặt hàng.   ĐH: "+ order.getOrderId();
            orderStatusing="Kiện hàng của bạn đã được đặt";
        }

        holder.orderStatus.setText(formattedDate +" "+ orderStatus);
        holder.orderStatusing.setText(orderStatusing);

        // Chỉ hiển thị các nút khi trạng thái là "Đã giao"
        if (order.getOrderStatus().equals("COMPLETED")) {
            holder.reorderButton.setVisibility(View.VISIBLE);
            holder.reviewButton.setVisibility(View.VISIBLE);
            holder.orderCancle.setVisibility(View.GONE);
            holder.viewFeedback.setVisibility(View.GONE);
            holder.btnPayment.setVisibility(View.GONE);

        }else if(order.getOrderStatus().equals("CONFIRMED")) {
            // Lấy LayoutParams hiện tại
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.orderCancle.getLayoutParams();

// Set marginStart theo ý bạn, ví dụ 16dp (chuyển ra pixel trước)
            int marginInDp = 130; // số dp bạn muốn
            float scale = holder.orderCancle.getContext().getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * scale + 0.5f);

            params.setMarginStart(marginInPx);

// Apply lại LayoutParams
            holder.orderCancle.setLayoutParams(params);
            holder.reorderButton.setVisibility(View.GONE);
            holder.reviewButton.setVisibility(View.GONE);
            holder.orderCancle.setVisibility(View.GONE);
            holder.viewFeedback.setVisibility(View.GONE);
            holder.btnPayment.setVisibility(View.GONE);

        }

        else if(order.getOrderStatus().equals("FEEDBACKED")) {

            ReviewAPI reviewAPI = APIRetrofit.getReviewAPIService();
            Call<Boolean> call = reviewAPI.checkReviewExists(order.getOrderItemID());

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean reviewExists = response.body();
                        if (reviewExists) {
                            // Nếu Review tồn tại, hiển thị viewFeedback
                            holder.reorderButton.setVisibility(View.VISIBLE);
                            holder.reviewButton.setVisibility(View.GONE);
                            holder.orderCancle.setVisibility(View.GONE);
                            holder.viewFeedback.setVisibility(View.VISIBLE);
                            holder.btnPayment.setVisibility(View.GONE);

                        } else {
                            // Nếu Review không tồn tại, hiển thị reviewButton
                            holder.reorderButton.setVisibility(View.VISIBLE);
                            holder.reviewButton.setVisibility(View.VISIBLE);
                            holder.orderCancle.setVisibility(View.GONE);
                            holder.viewFeedback.setVisibility(View.GONE);
                            holder.btnPayment.setVisibility(View.GONE);

                        }
                    } else {
                        Toast.makeText(context, "Lỗi kiểm tra trạng thái đánh giá", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(order.getOrderStatus().equals("CANCELLED")) {
            // Lấy LayoutParams hiện tại
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.reorderButton.getLayoutParams();
            int marginInDp = 130; // số dp bạn muốn
            float scale = holder.reorderButton.getContext().getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * scale + 0.5f);
            params.setMarginStart(marginInPx);
            holder.reorderButton.setLayoutParams(params);

// Hiển thị button
            holder.reorderButton.setVisibility(View.VISIBLE);
            holder.reviewButton.setVisibility(View.GONE);
            holder.orderCancle.setVisibility(View.GONE);
            holder.viewFeedback.setVisibility(View.GONE);
            holder.btnPayment.setVisibility(View.GONE);

        }
        else if (order.getOrderStatus().equals("PENDING")) {

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.orderCancle.getLayoutParams();
            int marginInDp = 10; // số dp bạn muốn
            float scale = holder.orderCancle.getContext().getResources().getDisplayMetrics().density;
            int marginInPx = (int) (marginInDp * scale + 0.5f);
            params.setMarginStart(marginInPx);


            holder.btnPayment.setLayoutParams(params);


            holder.reorderButton.setVisibility(View.GONE);
            holder.reviewButton.setVisibility(View.GONE);
            holder.orderCancle.setVisibility(View.VISIBLE);
            holder.viewFeedback.setVisibility(View.GONE);
            holder.btnPayment.setVisibility(View.VISIBLE);
        }
        else {
            holder.reorderButton.setVisibility(View.GONE);
            holder.reviewButton.setVisibility(View.GONE);
            holder.orderCancle.setVisibility(View.GONE);
            holder.viewFeedback.setVisibility(View.GONE);
            holder.btnPayment.setVisibility(View.GONE);

        }

        // Set ảnh sản phẩm
        Glide.with(context)
                .load(order.getImageUrl())
                .into(holder.productImage);


        // Thiết lập sự kiện nhấn vào nút "Viết đánh giá"
        holder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Truyền dữ liệu qua Intent
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.putExtra("orderItemId", order.getOrderItemID());  // Truyền orderItemId
                intent.putExtra("productImage", order.getImageUrl());   // Truyền URL hình ảnh sản phẩm
                intent.putExtra("productName", order.getProductName());  // Truyền tên sản phẩm
                intent.putExtra("color", order.getColor());
                intent.putExtra("orderId", order.getOrderId());
                // Mở FeedBackActivity
//                context.startActivity(intent);
                Activity activity = (Activity) context;
                intent.putExtra("position", holder.getAdapterPosition());
                activity.startActivityForResult(intent, 123); // requestCode = 123

            }
        });
        // Thiết lập sự kiện nhấn vào nút "View Feedback"
        holder.viewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi API để lấy thông tin review cho sản phẩm
                ReviewAPI reviewAPI = APIRetrofit.getReviewAPIService();
                Call<ReviewRequestDTO> call = reviewAPI.getReviewByOrderItemId(order.getOrderItemID());

                call.enqueue(new Callback<ReviewRequestDTO>() {
                    @Override
                    public void onResponse(Call<ReviewRequestDTO> call, Response<ReviewRequestDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ReviewRequestDTO reviewRequestDTO = response.body();

                            // Hiển thị dialog với dữ liệu nhận được từ ReviewRequestDTO
                            showFeedbackDialog(reviewRequestDTO, order);
                        } else {
                            Toast.makeText(context, "Không có thông tin đánh giá", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewRequestDTO> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.reorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Truyền dữ liệu qua Intent
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productName", order.getProductName());  // Truyền orderItemId

                // Mở FeedBackActivity
//                context.startActivity(intent);
                Activity activity = (Activity) context;
                intent.putExtra("position", holder.getAdapterPosition());
                activity.startActivityForResult(intent, 123); // requestCode = 123
            }
        });
        holder.linearLayout.setOnClickListener(v -> {
            // Khi nhấn vào LinearLayout, mở Dialog chi tiết đơn hàng
            showOrderDetailsDialog(order);

        });
        holder.productImage.setOnClickListener(v->{
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productName", order.getProductName());  // Truyền orderItemId

            // Mở FeedBackActivity
//                context.startActivity(intent);
            Activity activity = (Activity) context;
            intent.putExtra("position", holder.getAdapterPosition());
            activity.startActivityForResult(intent, 123); // requestCode = 123
        });

        holder.btnPayment.setOnClickListener(v->{
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra("orderId", order.getOrderId());  // Truyền orderItemId

            // Mở FeedBackActivity
//                context.startActivity(intent);
            Activity activity = (Activity) context;
            intent.putExtra("position", holder.getAdapterPosition());
            activity.startActivityForResult(intent, 123); // requestCode = 123
        });

        holder.orderCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog(order.getOrderId());
            }
        });

        // Set ảnh sản phẩm
        Glide.with(context)
                .load(order.getImageUrl())
                .into(holder.productImage);


    }
    AlertDialog dialog;
    private void showOrderDetailsDialog(OrderItemRequestDTO order) {
        // Tạo LayoutInflater để tạo view từ XML layout dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_order_details, null);

        // Lấy các TextView từ layout dialog để hiển thị dữ liệu
        TextView orderIdText = dialogView.findViewById(R.id.orderIdText);
        TextView productNameText = dialogView.findViewById(R.id.productNameText);
        TextView productQuantityText = dialogView.findViewById(R.id.productQuantityText);
        TextView productColorText = dialogView.findViewById(R.id.productColorText);
        TextView productPriceText = dialogView.findViewById(R.id.productPriceText);
        TextView customerNameText = dialogView.findViewById(R.id.customerNameText);
        TextView customerAddressText = dialogView.findViewById(R.id.customerAddressText);
        TextView customerPhoneText = dialogView.findViewById(R.id.customerPhoneText);
        ImageView imageView =dialogView.findViewById(R.id.productImage);
        // Cập nhật dữ liệu vào các TextView
        orderIdText.setText(" "+ order.getOrderId());
        productNameText.setText(" " + order.getProductName());
        productQuantityText.setText(" x" + order.getQuantity());
        productColorText.setText(" " + order.getColor());

        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Define the decimal format
        String formattedProductPrice = decimalFormat.format(order.getTotalPrice()); // Format the product price
        productPriceText.setText(" " + formattedProductPrice + "₫");
        customerNameText.setText(" " + order.getFullName());
        customerAddressText.setText("" + order.getAddress());
        customerPhoneText.setText("" + order.getPhoneNumber());
        Glide.with(context)
                .load(order.getImageUrl())
                .into(imageView);// Tạo AlertDialog để hiển thị

        // Tạo AlertDialog để hiển thị
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setCancelable(true);
        dialog = builder.create();
        dialog.show();

        // Thêm sự kiện nhấn nút "Đóng"
        TextView btnCloseDialog = dialogView.findViewById(R.id.btnCloseDialog);
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đóng dialog khi nhấn nút
                dialog.dismiss();
            }
        });
    }
    private void showFeedbackDialog(ReviewRequestDTO reviewRequestDTO, OrderItemRequestDTO order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_feedback, null);

        // Set dữ liệu vào dialog
        ImageView productImage = dialogView.findViewById(R.id.feedbackProductImage);
        TextView productName = dialogView.findViewById(R.id.feedbackProductName);
        RatingBar ratingBar = dialogView.findViewById(R.id.feedbackRating);
        TextView commentText = dialogView.findViewById(R.id.feedbackComment);
        RecyclerView imagesRecyclerView = dialogView.findViewById(R.id.feedbackImagesRecyclerView);

        // Load ảnh sản phẩm vào ImageView
        Glide.with(context).load(order.getImageUrl()).into(productImage);
        productName.setText(order.getProductName());
        ratingBar.setRating(reviewRequestDTO.getRatingValue());
        commentText.setText(reviewRequestDTO.getComment());

        // Hiển thị các hình ảnh đã đánh giá
        FeedbackImagesAdapter adapter = new FeedbackImagesAdapter(context, reviewRequestDTO.getImages());
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(context, 3)); // 3 cột
        imagesRecyclerView.setAdapter(adapter);

        builder.setView(dialogView)
                .setPositiveButton("Đóng", null)
                .create()
                .show();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ViewHolder cho mỗi item
    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productColor, productPrice, productTotal, orderStatus,orderStatusing,orderCancle,viewFeedback,productQuantity;
        ImageView productImage;
        TextView reviewButton, reorderButton , btnPayment;
        LinearLayout orderButtons ;
        // Khởi tạo LinearLayout
        LinearLayout linearLayout;

        public OrderProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productColor = itemView.findViewById(R.id.productColor);
            productPrice = itemView.findViewById(R.id.productPrice);
            productTotal = itemView.findViewById(R.id.productTotal);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderStatusing=itemView.findViewById(R.id.orderStatusing);
            productImage = itemView.findViewById(R.id.productImage);
            orderButtons = itemView.findViewById(R.id.orderButtons);
            reviewButton = itemView.findViewById(R.id.reviewButton);
            reorderButton = itemView.findViewById(R.id.reorderButton);
            orderCancle=itemView.findViewById(R.id.cancleOrderButton);
            viewFeedback=itemView.findViewById(R.id.viewFeedbackButton);
            productQuantity=itemView.findViewById(R.id.productQuantity);
            btnPayment=itemView.findViewById(R.id.btnPayment);
            linearLayout = itemView.findViewById(R.id.orderStatusLayout);
        }
    }
    private void showCancelDialog(int orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_cancel_order, null);

        builder.setView(dialogView)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi API hủy đơn hàng
                        OrderItemAPI api = APIRetrofit.getOrderItemAPIService();
                        Call<Void> call = api.cancelOrder(orderId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
                                    ((Activity) context).recreate(); // hoặc load lại dữ liệu
                                } else {
                                    Toast.makeText(context, "Hủy đơn thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Không", null)
                .create()
                .show();
    }


}

