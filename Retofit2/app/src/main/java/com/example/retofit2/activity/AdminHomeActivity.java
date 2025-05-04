package com.example.retofit2.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retofit2.R;
import com.example.retofit2.adapter.GridViewAdminAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView tvAdminName, tvAdminPhone;
    private String[] titles = {"Quản lý sản phẩm", "Quản lý đơn hàng", "Quản lý khách hàng", "Chi tiết thống kê", "Quản lí giảm giá", "Tư vấn khách hàng"};
    private int[] icons = {R.drawable.products, R.drawable.order, R.drawable.customer, R.drawable.statusupdate, R.drawable.coupon, R.drawable.livechat};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.admin_home);
        GridView gridView = findViewById(R.id.adminFeatures);
        GridViewAdminAdapter adapter = new GridViewAdminAdapter(this, titles, icons);
        gridView.setAdapter(adapter);


        // Set thông tin admin (tạm thời fix cứng)


        // Xử lý thanh điều hướng
        BottomNavigationView navBar = findViewById(R.id.adminNav);
        navBar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    // Ở trang chủ rồi nên không làm gì
                    return true;
                }
                // Tạm thời comment 2 chức năng này
                else if (id == R.id.nav_orders) {
                    // startActivity(new Intent(AdminHomeActivity.this, OrderManagementActivity.class));
                    // Giả lập hành động tạm thời
                    //tvAdminName.setText("Chức năng Đơn hàng đang cập nhật...");
                    return true;
                }
                else if (id == R.id.nav_home) {
                    // startActivity(new Intent(AdminHomeActivity.this, AdminAccountActivity.class));
                    //tvAdminName.setText("Chức năng Tài khoản đang cập nhật...");
                    return true;
                }
                return false;
            }

        });
    }
}
