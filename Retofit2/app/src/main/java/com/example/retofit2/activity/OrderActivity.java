package com.example.retofit2.activity;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.retofit2.R;

import com.example.retofit2.adapter.customer.BannerAdapter;
import com.example.retofit2.adapter.customer.OrderItemAdapter;
import com.example.retofit2.adapter.customer.OrderStatusAdapter;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.OrderItemRequestDTO;
import com.example.retofit2.dto.requestDTO.OrderStatusRequestDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private OrderItemAdapter orderProductAdapter;
    private List<OrderItemRequestDTO> orderList;
    private Handler handler = new Handler();
    private int currentBannerIndex = 0;
    private Runnable bannerRunnable;
    private List<Integer> bannerImages;
    private ViewPager2 bannerViewPager;
    private  BannerAdapter bannerAdapter;


    private RecyclerView statusOrderRecyclerView;
    private OrderStatusAdapter statusAdapter;
    private List<OrderStatusRequestDTO> statusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_order_home);
        bannerViewPager = findViewById(R.id.bannerViewPager);
        setupBanner();
        // Khởi tạo RecyclerView
        orderRecyclerView = findViewById(R.id.orderProductRecyclerView);
        // Tạo dữ liệu giả
       Long userId = getIntent().getLongExtra("userId", -1);
       // int userId=1;
        int userIdInt = userId.intValue();

        getAllOrderItem(1);
        // Gán adapter cho RecyclerView
        addTooltipToTextViews();

        // Trạng thái đơn hàng
        statusOrderRecyclerView = findViewById(R.id.statusOrderRecyclerView);
        statusList = createOrderStatuses();
        statusAdapter = new OrderStatusAdapter(this, statusList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        statusOrderRecyclerView.setLayoutManager(layoutManager);
        statusOrderRecyclerView.setAdapter(statusAdapter);


        BottomNavigationView bottomNavigationView = findViewById(R.id.adminNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(OrderActivity.this, CustomerHomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                // Tạm thời comment 2 chức năng này
                else if (id == R.id.nav_orders) {

                    return true;

                } else if (id == R.id.nav_home) {
                    // startActivity(new Intent(AdminHomeActivity.this, AdminAccountActivity.class));
                    //tvAdminName.setText("Chức năng Tài khoản đang cập nhật...");
                    return true;
                }
                return false;
            }

        });

        statusAdapter.setOnStatusClickListener(statusName -> {
            if ("Tất cả đơn".equals(statusName)) {
                getAllOrderItem(1); // lấy tất cả đơn
            } else {
                // Mapping tên tiếng Việt -> statusCode bên API nếu cần
                String apiStatus = convertStatusToAPIFormat(statusName);
                getOrderStatusItem(1, apiStatus);
            }
        });


    }
    private void getAllOrderItem(int customerId) {
        APIRetrofit.getOrderItemAPIService().getAllOrderItem(customerId).enqueue(new Callback<List<OrderItemRequestDTO>>() {
            @Override
            public void onResponse(Call<List<OrderItemRequestDTO>> call, Response<List<OrderItemRequestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList = response.body(); // Gán dữ liệu trả về vào orderList
                    orderProductAdapter = new OrderItemAdapter(OrderActivity.this, orderList);
                    orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
                    orderRecyclerView.setAdapter(orderProductAdapter);
                } else {
                    Toast.makeText(OrderActivity.this, "Dữ liệu rỗng hoặc không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderItemRequestDTO>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getOrderStatusItem(int customerId, String orderStatus) {
        APIRetrofit.getOrderItemAPIService().getOrderStatusItem(customerId, orderStatus)
                .enqueue(new Callback<List<OrderItemRequestDTO>>() {
                    @Override
                    public void onResponse(Call<List<OrderItemRequestDTO>> call, Response<List<OrderItemRequestDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            orderList = response.body();
                            orderProductAdapter = new OrderItemAdapter(OrderActivity.this, orderList);
                            orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
                            orderRecyclerView.setAdapter(orderProductAdapter);
                        } else {
                            Toast.makeText(OrderActivity.this, "Không có đơn hàng cho trạng thái này", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<OrderItemRequestDTO>> call, Throwable t) {
                        Toast.makeText(OrderActivity.this, "Lỗi khi tải đơn hàng theo trạng thái", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String convertStatusToAPIFormat(String statusName) {
        switch (statusName) {
            case "Đã đặt hàng":
                return "CONFIRMED";
            case "Đang giao hàng":
                return "SHIPPED";
            case "Đã giao hàng":
                return "COMPLETED";
            case "Đã hủy":
                return "CANCELLED";
            case "Đã đánh giá":
                return "FEEDBACKED";
            default:
                return "";
        }
    }

    private List<OrderStatusRequestDTO> createOrderStatuses() {
        List<OrderStatusRequestDTO> list = new ArrayList<>();

        OrderStatusRequestDTO status0 = new OrderStatusRequestDTO();
        status0.setNameOrderStatus("Tất cả đơn");
        status0.setImageOrderStatus("checkout"); // drawable/order.png

        OrderStatusRequestDTO status1 = new OrderStatusRequestDTO();
        status1.setNameOrderStatus("Đã đặt hàng");
        status1.setImageOrderStatus("manifest"); // drawable/order.png

        OrderStatusRequestDTO status2 = new OrderStatusRequestDTO();
        status2.setNameOrderStatus("Đang giao hàng");
        status2.setImageOrderStatus("expressdelivery"); // drawable/delivery.png

        OrderStatusRequestDTO status3 = new OrderStatusRequestDTO();
        status3.setNameOrderStatus("Đã giao hàng");
        status3.setImageOrderStatus("received"); // drawable/delivered.png

        OrderStatusRequestDTO status4 = new OrderStatusRequestDTO();
        status4.setNameOrderStatus("Đã hủy");
        status4.setImageOrderStatus("cancelorder");

        OrderStatusRequestDTO status5 = new OrderStatusRequestDTO();
        status5.setNameOrderStatus("Đã đánh giá");
        status5.setImageOrderStatus("feedback"); // drawable/delivered.png// drawable/delivered.png

        list.add(status0);
        list.add(status1);
        list.add(status2);
        list.add(status3);
        list.add(status4);
        list.add(status5);
        return list;
    }


    private void addTooltipToTextViews() {
        for (int i = 0; i < orderRecyclerView.getChildCount(); i++) {
            LinearLayout orderItemLayout = (LinearLayout) orderRecyclerView.getChildAt(i);
            TextView productNameTextView = orderItemLayout.findViewById(R.id.productName);
            // Kiểm tra nếu API >= 26, mới áp dụng tooltip
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                productNameTextView.setTooltipText(productNameTextView.getText());
            }
        }
    }
    private void setupBanner() {
        // Thêm ảnh demo cho banner (có thể thay bằng URL từ API)
        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.banner6);
        bannerImages.add(R.drawable.banner7);
        bannerImages.add(R.drawable.banner1);
        bannerImages.add(R.drawable.banner2);
        bannerImages.add(R.drawable.banner3);
        bannerImages.add(R.drawable.banner4);

        // Khởi tạo adapter cho ViewPager2
        bannerAdapter = new BannerAdapter(OrderActivity.this, bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        // Thiết lập chấm tròn cho ViewPager2
        WormDotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.setViewPager2(bannerViewPager);
        // Thay đổi màu sắc của chấm (ở đây là màu trắng)
        dotsIndicator.setStrokeDotsIndicatorColor(Color.GRAY); // Thử thay đổi màu sắc thành màu đỏ
        // Thử thay đổi màu sắc thành màu đỏ
        dotsIndicator.setScrollBarSize(12);
        // Kích thước chấm là 12dp

// Nếu bạn muốn thay đổi màu của các chấm khi chúng được chọn
        dotsIndicator.setDotIndicatorColor(Color.GRAY);

        // Tự động chuyển banner sau mỗi 3 giây
        bannerRunnable = () -> {
            if (currentBannerIndex >= bannerImages.size()) {
                currentBannerIndex = 0;
            }
            bannerViewPager.setCurrentItem(currentBannerIndex++, true);
            handler.postDelayed(bannerRunnable, 1000);
        };

        handler.postDelayed(bannerRunnable, 1000);

        // Lắng nghe thay đổi trang trên ViewPager2
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentBannerIndex = position;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            boolean updated = data.getBooleanExtra("feedbackUpdated", false);
            int position = data.getIntExtra("position", -1);

            if (updated) {
                getAllOrderItem(2); // load lại dữ liệu

                new Handler().postDelayed(() -> {
                    if (position != -1) {
                        orderRecyclerView.scrollToPosition(position);
                    }
                }, 500); // delay để đảm bảo dữ liệu đã load xong
            }
        }
    }

}
