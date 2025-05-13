package com.example.retofit2.activity;

import static android.graphics.Color.red;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.BannerAdapter;
import com.example.retofit2.adapter.customer.HomeCatagoryAdapter;
import com.example.retofit2.adapter.customer.HomeProductAdapter;
import com.example.retofit2.api.retrofit.CategoryRetrofit;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.CategoryRequestDTO;
import com.example.retofit2.dto.requestDTO.ProductRequestDTO;
import com.example.retofit2.utils.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeActivity extends AppCompatActivity implements HomeCatagoryAdapter.OnCategoryClickListener {
    private RecyclerView categoryRecyclerView, productRecyclerView;
    private HomeCatagoryAdapter categoryAdapter;
    private HomeProductAdapter productAdapter;
    private ViewPager2 bannerViewPager;
    private  BannerAdapter bannerAdapter;
    private SearchView searchInput;
    List<ProductRequestDTO> productList=new ArrayList<>();
    private Handler handler = new Handler();
    private int currentBannerIndex = 0;
    private Runnable bannerRunnable;
    private List<Integer> bannerImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home);

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        searchInput = findViewById(R.id.search_input);
        bannerViewPager = findViewById(R.id.bannerViewPager);

        setupBanner();
        getAllCategories();
        getAllProducts();

        // Thêm sự kiện cho SearchView
        SearchView searchView = findViewById(R.id.search_input);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;  // Không làm gì khi nhấn submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListener(newText);  // Lọc dữ liệu khi người dùng gõ
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.adminNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
     //   Long userId = getIntent().getLongExtra("userId", -1);
        Long userId= SharedPrefManager.getUserId();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    // Ở trang chủ rồi nên không làm gì
                    return true;
                }
                // Tạm thời comment 2 chức năng này
                else if (id == R.id.nav_orders) {
                    Intent intent = new Intent(CustomerHomeActivity.this, OrderActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                    return true;

                } else if (id == R.id.nav_home) {

                    return true;
                }
                else if (id == R.id.nav_account) {
                    Intent intent = new Intent(CustomerHomeActivity.this, ProfileActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                return true;
            }

                return false;
            }

        });

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
        bannerAdapter = new BannerAdapter(CustomerHomeActivity.this, bannerImages);
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
    private void filterListener(String text) {
        List<ProductRequestDTO> list = new ArrayList<>();
        // Lọc danh sách các item dựa trên mô tả (description)
        for (ProductRequestDTO productRequestDTO : productList) {
            if (productRequestDTO.getProductName().toLowerCase().contains(text.toLowerCase())) {
                list.add(productRequestDTO);
            }
        }

        // Nếu danh sách trống, hiển thị Toast thông báo
        if (list.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            // Cập nhật danh sách vào Adapter
            productAdapter.setListenerList(list);
        }
    }

    private void getAllProducts() {
        // Gọi API để lấy danh sách sản phẩm
        APIRetrofit.getApiService().getAllProducts().enqueue(new Callback<List<ProductRequestDTO>>() {
            @Override
            public void onResponse(Call<List<ProductRequestDTO>> call, Response<List<ProductRequestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body();
                    // Cập nhật danh sách sản phẩm vào RecyclerView
                    productRecyclerView.setLayoutManager(new GridLayoutManager(CustomerHomeActivity.this, 2));

                    productAdapter = new HomeProductAdapter(productList);
                    productRecyclerView.setAdapter(productAdapter);
                } else {
                    Toast.makeText(CustomerHomeActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductRequestDTO>> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getAllCategories() {
        CategoryRetrofit.getApiService().getAllCategories().enqueue(new Callback<List<CategoryRequestDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryRequestDTO>> call, Response<List<CategoryRequestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CategoryRequestDTO> categoryList = response.body();

                    // Thêm danh mục "Tất cả"
                    CategoryRequestDTO allCategory = new CategoryRequestDTO();
                    allCategory.setCategoryID(0); // ID 0 đại diện cho "Tất cả"
                    allCategory.setCategoryName("Tất cả");

                    categoryList.add(0, allCategory); // Đưa "Tất cả" lên đầu danh sách

                    categoryRecyclerView.setLayoutManager(new LinearLayoutManager(CustomerHomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    // Truyền sự kiện click
                    categoryAdapter = new HomeCatagoryAdapter(categoryList, CustomerHomeActivity.this);
                    categoryRecyclerView.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryRequestDTO>> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Khi chọn một danh mục, gọi API để lấy sản phẩm theo danh mục
    @Override
    public void onCategoryClick(int categoryId) {
        if (categoryId == 0) {
            // Nếu chọn "Tất cả", gọi API lấy toàn bộ sản phẩm
            getAllProducts();
        } else {
            // Nếu chọn danh mục cụ thể, gọi API lấy sản phẩm theo danh mục
            getPhonesByCategory(categoryId);
        }
    }


    private void getPhonesByCategory(int categoryId) {
        CategoryRetrofit.getApiService().getPhonesByCategory(categoryId).enqueue(new Callback<List<ProductRequestDTO>>() {
            @Override
            public void onResponse(Call<List<ProductRequestDTO>> call, Response<List<ProductRequestDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductRequestDTO> productList = response.body();
                    productRecyclerView.setLayoutManager(new GridLayoutManager(CustomerHomeActivity.this, 2));
                    productAdapter = new HomeProductAdapter(productList);
                    productRecyclerView.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ProductRequestDTO>> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
