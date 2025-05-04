package com.example.retofit2.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.retofit2.R;
import com.example.retofit2.adapter.customer.ImageAdapter;
import com.example.retofit2.adapter.customer.ImageViewHolder2Adapter;
import com.example.retofit2.adapter.customer.ReviewAdapter;
import com.example.retofit2.dto.requestDTO.Review;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewPager2 mainImageView; // ViewPager2 để lướt qua hình ảnh chính
    private ImageButton prevButton, nextButton;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    private Integer[] imageResources = {
            R.drawable.iphone1, R.drawable.iphone2, R.drawable.iphone3, R.drawable.iphone4, R.drawable.iphone1
    };

    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        recyclerView = findViewById(R.id.itemRecyclerView);
        mainImageView = findViewById(R.id.mainImageView); // ViewPager2 thay cho ImageView
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        // Cập nhật ViewPager2 để hiển thị hình ảnh chính
        ImageViewHolder2Adapter viewPagerAdapter = new ImageViewHolder2Adapter(this, imageResources);
        mainImageView.setAdapter(viewPagerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //Thiet lap adapter cho RecycleView
        ImageAdapter adapter = new ImageAdapter(this, imageResources, position -> {
            currentPosition = position;
            mainImageView.setCurrentItem(currentPosition, true);
        });
        recyclerView.setAdapter(adapter);

        // Xử lý nút "Previous"
        prevButton.setOnClickListener(v -> {
            if (currentPosition > 0) {
                currentPosition--;
                mainImageView.setCurrentItem(currentPosition);
                adapter.setSelectedPosition(currentPosition);
            } else if(currentPosition == 0) {
                currentPosition = imageResources.length - 1;
                mainImageView.setCurrentItem(currentPosition);
                adapter.setSelectedPosition(currentPosition);
            }
        });

        // Xử lý nút "Next"
        nextButton.setOnClickListener(v -> {
            if (currentPosition < imageResources.length - 1) {
                currentPosition++;
                mainImageView.setCurrentItem(currentPosition);
                adapter.setSelectedPosition(currentPosition);
            } else if(currentPosition == imageResources.length - 1){
                currentPosition = 0;
                mainImageView.setCurrentItem(currentPosition);
                adapter.setSelectedPosition(currentPosition);
            }
        });


        /*    Danh sách đánh giá   */
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        // Khởi tạo danh sách đánh giá mẫu
        List<String> reviewImages = new ArrayList<>();
        reviewImages.add("https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-14-plus_1_.png");
        reviewImages.add("https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-15-plus-256gb_2.png");

        reviewList = new ArrayList<>();
        reviewList.add(new Review("q*****1", 4, "Đúng hàng chính hãng Apple! Nguyên seal! Nguyên tem màu!", "2 ngày trước", reviewImages));
        reviewList.add(new Review("t*****2", 5, "Mua rất hài lòng! Sản phẩm đẹp và xịn.", "1 tuần trước", reviewImages));

        // Thiết lập RecyclerView
        reviewAdapter = new ReviewAdapter(reviewList);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setAdapter(reviewAdapter);




        /* Add Cart */
        ImageView cartIcon = findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_cart, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            //load du lieu vao modal
            loadProductData(bottomSheetView);

            //Hien thi BottomSheet
            bottomSheetDialog.show();
        });

    }

    private void loadProductData(View bottomSheetView) {
        //Cập nhật giá sản phẩm
        TextView priceText = bottomSheetView.findViewById(R.id.priceText);
        priceText.setText("₫30.790.000");

        //Cập nhật kho
        TextView stockText = bottomSheetView.findViewById(R.id.stockText);
        stockText.setText("Kho: 24");

        //Cap nhat anh san pham
        ImageView productImage = bottomSheetView.findViewById(R.id.productImage);
        productImage.setImageResource(R.drawable.iphone1);

        //Load mau sac cho lua chon
        LinearLayout colorOptionsLayout = bottomSheetView.findViewById(R.id.colorOptionsLayout);
        String[] colorNames = {"Titan Tự Nhiên", "Titan Trắng", "Titan Đen", "Titan Sa Mạc"};
        int[] imageRes = {R.drawable.iphone1, R.drawable.iphone2, R.drawable.iphone3, R.drawable.iphone4};

        for(int i = 0 ; i < colorNames.length; i ++){
            View colorItem = LayoutInflater.from(this).inflate(R.layout.color_option_item, null);
            ImageView colorImage = colorItem.findViewById(R.id.colorImage);
            TextView colorName = colorItem.findViewById(R.id.colorName);

            colorImage.setImageResource(imageRes[i]);
            colorName.setText(colorNames[i]);

            // Thêm margin giữa các item
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 25, 0);
            colorItem.setLayoutParams(params);


            int finalI = i;
            colorItem.setOnClickListener(v -> {
                //Reset selected state
                for(int j = 0; j < colorOptionsLayout.getChildCount(); j ++){
                    colorOptionsLayout.getChildAt(j).setSelected(false);
                }
                v.setSelected(true);

            });
            colorOptionsLayout.addView(colorItem);
        }

        // Cập nhật số lượng (và các nút cộng, trừ)
        TextView quantityText = bottomSheetView.findViewById(R.id.quantityText);
        ImageButton minusBtn = bottomSheetView.findViewById(R.id.minusButton);
        ImageButton plusBtn = bottomSheetView.findViewById(R.id.plusButton);

        final int[] quantity = {1};
        minusBtn.setOnClickListener(v -> {
            if (quantity[0] > 1) quantityText.setText(String.valueOf(--quantity[0]));
        });
        plusBtn.setOnClickListener(v -> {
            quantityText.setText(String.valueOf(++quantity[0]));
        });
    }
}