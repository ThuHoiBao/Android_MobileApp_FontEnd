package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.retofit2.R;
import com.example.retofit2.Toast.CustomToast;
import com.example.retofit2.adapter.customer.ImageAdapter;
import com.example.retofit2.adapter.customer.ImageViewHolder2Adapter;
import com.example.retofit2.adapter.customer.ReviewAdapter;
import com.example.retofit2.api.ICartAPI;
import com.example.retofit2.api.ProductAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.AddProductToCartRequestDTO;
import com.example.retofit2.dto.requestDTO.ProductSummaryDTO;
import com.example.retofit2.dto.requestDTO.ProductVariantDTO;
import com.example.retofit2.dto.requestDTO.ReviewDTO;
import com.example.retofit2.dto.responseDTO.ResponseObject;
import com.example.retofit2.utils.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewPager2 mainImageView; // ViewPager2 để lướt qua hình ảnh chính
    private ImageButton prevButton, nextButton;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<ReviewDTO> reviewList;
    private ImageAdapter adapter;
    private ProductSummaryDTO productSummaryDTO;

    private TextView productName, price, soldCount;

    private int currentPosition = 0;
    private View bottomSheetView;
    private List<ProductVariantDTO> productVariants = new ArrayList<>(); // ✅
    private int stockColorItem;
    private String product_name;
    private ProductVariantDTO selectedVariant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        recyclerView = findViewById(R.id.itemRecyclerView);
        mainImageView = findViewById(R.id.mainImageView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        productName = findViewById(R.id.productNameTextView);
        price = findViewById(R.id.priceTextView);
        soldCount = findViewById(R.id.soldQuantityTextView);

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
                    Intent intent = new Intent(ProductDetailActivity.this, OrderActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                    return true;

                } else if (id == R.id.nav_home) {

                    return true;
                }
                else if (id == R.id.nav_account) {
                    Intent intent = new Intent(ProductDetailActivity.this, ProfileActivity.class);
                    intent.putExtra("userId", userId); // userId có thể là String hoặc int, tùy bạn
                    startActivity(intent);
                    return true;
                } else if (id == R.id.nav_cart){
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }

                return false;
            }

        });

        Intent intent = getIntent();
        product_name = intent.getStringExtra("productName");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Xử lý nút "Previous"
        prevButton.setOnClickListener(v -> {
            if (adapter == null) return;
            if (currentPosition > 0) {
                currentPosition--;
                updatePrice(currentPosition);
            } else {
                currentPosition = adapter.getItemCount() - 1;
                updatePrice(currentPosition);
            }
            mainImageView.setCurrentItem(currentPosition);
            adapter.setSelectedPosition(currentPosition);
        });

        // Xử lý nút "Next"
        nextButton.setOnClickListener(v -> {
            if (adapter == null) return;
            if (currentPosition < adapter.getItemCount() - 1) {
                currentPosition++;
                updatePrice(currentPosition);
            } else {
                currentPosition = 0;
                updatePrice(currentPosition);
            }
            mainImageView.setCurrentItem(currentPosition);
            adapter.setSelectedPosition(currentPosition);
        });

        // Cập nhật khi vuốt ViewPager2
        mainImageView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (adapter != null) {
                    adapter.setSelectedPosition(position);
                }
            }
        });

        /* Danh sách đánh giá */
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
//        List<String> reviewImages = new ArrayList<>();
//        reviewImages.add("https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-14-plus_1_.png");
//        reviewImages.add("https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-15-plus-256gb_2.png");
//
//        reviewList = new ArrayList<>();
//        reviewList.add(new ReviewDTO("q*****1", 4, "Đúng hàng chính hãng Apple! Nguyên seal! Nguyên tem màu!", "2 ngày trước", reviewImages));
//        reviewList.add(new ReviewDTO("t*****2", 5, "Mua rất hài lòng! Sản phẩm đẹp và xịn.", "1 tuần trước", reviewImages));
//
//        reviewAdapter = new ReviewAdapter(reviewList);
//        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        reviewRecyclerView.setAdapter(reviewAdapter);

        /* Add to Cart */
        ImageView cartIcon = findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductDetailActivity.this);
            bottomSheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_cart, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
            loadProductData(bottomSheetView);
        });


        // Gọi API lấy thông tin sản phẩm
        callAPIProductDetail();
        if(!product_name.isEmpty()){
            callAPIReviewProductName(product_name);
        }



    }

    private void callAPIReviewProductName(String produc_name) {
        APIRetrofit.getReviewAPIService().getReviewsByProductName(produc_name).enqueue(new Callback<List<ReviewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList = response.body();
                    Log.d("reviewList", "Extracted User Id: " + reviewList);

                    reviewAdapter = new ReviewAdapter(reviewList);
                    reviewRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this));
                    reviewRecyclerView.setAdapter(reviewAdapter);
                } else {
                    Log.d("reviewList", "Extracted User Id: " + reviewList);
                }
            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePrice(int position) {
        if (productVariants != null && position < productVariants.size()) {
            double selectedPrice = productVariants.get(position).getPrice();
            price.setText("₫" + String.format("%,.0f", selectedPrice));
        }
    }

    private void callAPIProductDetail() {
        ProductAPI productAPI = APIRetrofit.getRetrofitInstance().create(ProductAPI.class);
        Call<ProductSummaryDTO> call = productAPI.getProductSummary(product_name);

        // In ra URL đang gọi
        Log.d("API_URL", call.request().url().toString());

        call.enqueue(new Callback<ProductSummaryDTO>() {
            @Override
            public void onResponse(Call<ProductSummaryDTO> call, Response<ProductSummaryDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productSummaryDTO = response.body();
                    updateProductInfo();
                }
            }

            @Override
            public void onFailure(Call<ProductSummaryDTO> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductInfo() {
        if (productSummaryDTO != null) {
            productName.setText(productSummaryDTO.getProductName());
            soldCount.setText("Đã bán: " + productSummaryDTO.getSoldCount());

            if (!productSummaryDTO.getVariants().isEmpty()) {
                productVariants = productSummaryDTO.getVariants();
                updatePrice(0); // Hiển thị giá đầu tiên

                List<String> imageUrls = new ArrayList<>();
                for (ProductVariantDTO variant : productSummaryDTO.getVariants()) {
                    imageUrls.add(variant.getImageUrl());
                }

                // ViewPager2
                ImageViewHolder2Adapter viewPagerAdapter = new ImageViewHolder2Adapter(ProductDetailActivity.this, imageUrls);
                mainImageView.setAdapter(viewPagerAdapter);

                // RecyclerView thumbnails
                adapter = new ImageAdapter(ProductDetailActivity.this, imageUrls, position -> {
                    currentPosition = position;
                    mainImageView.setCurrentItem(position, true);
                    updatePrice(position);
                });
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private void loadProductData(View bottomSheetView) {
        if (productSummaryDTO != null) {
            // Cập nhật giá sản phẩm
            Button orderButton = bottomSheetView.findViewById(R.id.addToCartButton);
            TextView priceText = bottomSheetView.findViewById(R.id.priceText);
            double productPrice = productSummaryDTO.getVariants().get(0).getPrice(); // Lấy giá từ variant đầu tiên
            priceText.setText("₫" + String.format("%,.0f", productPrice));

            // Cập nhật kho
            TextView stockText = bottomSheetView.findViewById(R.id.stockText);
            TextView quantityText = bottomSheetView.findViewById(R.id.quantityText);
            int totalStock;
            totalStock = productSummaryDTO.getVariants().get(0).getStock(); // Cộng dồn số lượng kho
            stockText.setText("Kho: " + totalStock);

            // Cập nhật ảnh sản phẩm
            ImageView productImage = bottomSheetView.findViewById(R.id.productImage);
            String imageUrl = productSummaryDTO.getVariants().get(0).getImageUrl();
            Picasso.get().load(imageUrl).placeholder(R.drawable.iphone1).error(R.drawable.error_image).into(productImage);
//            productImage.setImageResource(R.drawable.iphone1); // Hoặc tải ảnh từ URL nếu có

            // Load màu sắc cho lựa chọn
            LinearLayout colorOptionsLayout = bottomSheetView.findViewById(R.id.colorOptionsLayout);
            for (int i = 0; i < productSummaryDTO.getVariants().size(); i++) {
                ProductVariantDTO variant = productSummaryDTO.getVariants().get(i);
                View colorItem = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.color_option_item, null);
                ImageView colorImage = colorItem.findViewById(R.id.colorImage);
                TextView colorName = colorItem.findViewById(R.id.colorName);
                // Cập nhật màu sắc và hình ảnh
                colorName.setText(variant.getColor());

                // Nếu có URL ảnh, có thể tải ảnh trực tuyến tại đây
                // Glide.with(ProductDetailActivity.this).load(variant.getImageUrl()).into(colorImage);
//                colorImage.setImageResource(R.drawable.iphone1); // Placeholder, thay bằng URL nếu cần
                Picasso.get().load(variant.getImageUrl()).placeholder(R.drawable.iphone1).error(R.drawable.error_image).into(colorImage);

                // Thêm margin giữa các item
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 25, 0);
                colorItem.setLayoutParams(params);

                final int variantIndex = i;
                if (variant.getStock() > 0) {
                    colorItem.setOnClickListener(v -> {
                        // Reset trạng thái đã chọn cho các item khác
                        quantityText.setText(String.valueOf(1));
                        for (int j = 0; j < colorOptionsLayout.getChildCount(); j++) {
                            colorOptionsLayout.getChildAt(j).setSelected(false);
                        }
                        v.setSelected(true);  // Đánh dấu màu sắc đã chọn

                        ProductVariantDTO selectedVariantDTO  = productSummaryDTO.getVariants().get(variantIndex);
                        //Cap nhat hinh anh
                        Picasso.get().load(selectedVariantDTO.getImageUrl()).placeholder(R.drawable.iphone1).error(R.drawable.error_image).into(productImage);
                        //Cap nhat gia
                        priceText.setText("₫" + String.format("%,.0f", selectedVariantDTO.getPrice()));
                        stockText.setText("Kho: " + selectedVariantDTO.getStock());
                        stockColorItem = selectedVariantDTO.getStock();

                        // Lưu variant đã chọn
                        selectedVariant = selectedVariantDTO;
                        Log.d("CartAdapter", "Clicked on item: " + selectedVariant.getColor());
                    });
                } else {
                    // Variant hết hàng, vô hiệu hoá
                    colorItem.setAlpha(0.7f); // Làm mờ
                    colorItem.setEnabled(false);
                    quantityText.setText(String.valueOf(0));
                }
                colorOptionsLayout.addView(colorItem);
            }

            // Cập nhật số lượng (và các nút cộng, trừ)

            ImageButton minusBtn = bottomSheetView.findViewById(R.id.minusButton);
            ImageButton plusBtn = bottomSheetView.findViewById(R.id.plusButton);
            Button addToCart = bottomSheetView.findViewById(R.id.addToCartButton);

            final int[] quantity = {1};
            final int[] currentStock = {productSummaryDTO.getVariants().get(0).getStock()}; // Bắt đầu với variant đầu tiên

            minusBtn.setOnClickListener(v -> {
                if (quantity[0] > 1) {
                    quantityText.setText(String.valueOf(--quantity[0]));
                }
            });
            plusBtn.setOnClickListener(v -> {
                if(quantity[0] > 5 && stockColorItem > 5){
                    CustomToast.makeText(this,
                            "Chỉ mua tối đa " + 5 + " sản phẩm!",
                            CustomToast.LONG,
                            CustomToast.WARNING,
                            true,
                            Gravity.TOP, 350, 100,
                            v1 -> {
                                // handle click if needed
                            }).show();
                }
                if (quantity[0] < stockColorItem) {
                    quantityText.setText(String.valueOf(++quantity[0]));
                } else {
                    CustomToast.makeText(this,
                            "Chỉ mua tối đa " + stockColorItem + " sản phẩm!",
                            CustomToast.LONG,
                            CustomToast.WARNING,
                            true,
                            Gravity.TOP, 350, 100,
                            v1 -> {
                                // handle click if needed
                            }).show();
                }
            });

            orderButton.setOnClickListener(v -> {
                if(selectedVariant == null){
                    CustomToast.makeText(this,
                            "Vui lòng chọn phân loại hàng!",
                            CustomToast.LONG,
                            CustomToast.WARNING,
                            true,
                            Gravity.TOP, 350, 100,
                            v1 -> {
                                // Xử lý khi người dùng click vào Toast nếu cần
                            }).show();
                } else {
                    //Đã chọn sản phẩm, gọi API để thêm vào giỏ hàng
                    AddProductToCartRequestDTO requestDTO = new AddProductToCartRequestDTO();
                    requestDTO.setProduct_name(product_name);
                    requestDTO.setColor(selectedVariant.getColor());
                    requestDTO.setQuantity(Integer.parseInt(quantityText.getText().toString()));

                    long userId = SharedPrefManager.getUserId();
                    String token = SharedPrefManager.getToken();

                    ICartAPI apiService = APIRetrofit.getCartAPIService(token);

                    // Gọi API thêm sản phẩm vào giỏ hàng
                    apiService.addProductToCart(requestDTO, userId).enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            if (response.isSuccessful()) {
                                // API thành công, hiển thị thông báo hoặc chuyển trang
                                CustomToast.makeText(ProductDetailActivity.this,
                                        "Sản phẩm đã được thêm vào giỏ hàng thành công!",
                                        CustomToast.LONG,
                                        CustomToast.SUCCESS,
                                        true,
                                        Gravity.TOP, 350, 100,
                                        v1 -> {
                                            // Handle click if needed
                                        }).show();
                            } else {
                                // API thất bại, thông báo lỗi
                                CustomToast.makeText(ProductDetailActivity.this,
                                        "Có lỗi khi thêm sản phẩm vào giỏ hàng!",
                                        CustomToast.LONG,
                                        CustomToast.ERROR,
                                        true,
                                        Gravity.TOP, 350, 100,
                                        v1 -> {
                                            // Handle click if needed
                                        }).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            // Lỗi khi gọi API
                            CustomToast.makeText(ProductDetailActivity.this,
                                    "Lỗi kết nối, vui lòng thử lại!",
                                    CustomToast.LONG,
                                    CustomToast.ERROR,
                                    true,
                                    Gravity.TOP, 350, 100,
                                    v1 -> {
                                        // Handle click if needed
                                    }).show();
                        }
                    });
                }
            });
        }
    }
}