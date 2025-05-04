package com.example.retofit2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.retofit2.R;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.responseDTO.ReviewResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity {

    // Declare views
    private ImageView addImage;
    private LinearLayout imageContainer;
    private ArrayList<Uri> imageUris = new ArrayList<>(); // To hold the image URIs

    private ImageView productImage,backImage;
    private TextView productName,reviewButton;

    private ReviewResponseDTO reviewDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_feedback); // Use the XML layout you created
        reviewDTO = new ReviewResponseDTO(); // thêm dòng này trước khi set các field

        // Initialize views
        addImage = findViewById(R.id.addImage);
        imageContainer = findViewById(R.id.imageContainer);
        reviewButton=findViewById(R.id.reviewButton);

        // Khởi tạo các View
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);

        backImage=findViewById(R.id.imageBack);
        // Sau đoạn Glide.with(this)... add thêm:
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("feedbackUpdated", true);
                resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        int orderItemId = getIntent().getIntExtra("orderItemId", -1);
        String productImageUrl = intent.getStringExtra("productImage");
        String productNameText = intent.getStringExtra("productName");
        String color=intent.getStringExtra("color");
        int orderId = getIntent().getIntExtra("orderId", -1);




        // Hiển thị dữ liệu lên giao diện
        productName.setText(productNameText);

        // Tải ảnh sản phẩm vào ImageView
        Glide.with(this)
                .load(productImageUrl)
                .into(productImage);

        // Set up the event for the "Add Image" button
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText commentInput = findViewById(R.id.commentInput);
                RatingBar ratingBar = findViewById(R.id.ratingBar);
                String comment = commentInput.getText().toString();
                int rating = (int) ratingBar.getRating();
                reviewDTO.setComment(comment);
                reviewDTO.setRatingValue(rating);
                reviewDTO.setProductName(productNameText);
                reviewDTO.setOrderId(orderId);
                reviewDTO.setColor(color);
                //reviewDTO.setDate(new Date());


                // Gọi API upload
                uploadImageToServer();
            }
        });

    }

    // Open image picker for adding an image (image or video can be picked)
    public void openImagePicker() {
        // Intent to open gallery or file picker
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Allow images
        startActivityForResult(intent, 1); // 1 is a request code for image selection
    }

    // Handle the result from image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // The user picked an image, get the URI
            Uri imageUri = data.getData();
            imageUris.add(imageUri);

            // Add the image to the layout
            addImageToLayout(imageUri);
        }
    }
    // Upload ảnh lên backend
    private void uploadImageToServer() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH);
        Date now = new Date();
        reviewDTO.setDate(now); // Gán ngày mới

        // Khởi tạo Gson có định dạng Date đúng với backend
        Gson gson = new GsonBuilder()
                .setDateFormat("MMM d, yyyy h:mm:ss a")
                .create();

        String reviewString = gson.toJson(reviewDTO); // Convert thành JSON string

        RequestBody reviewRequestBody = RequestBody.create(MediaType.parse("application/json"), reviewString);

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : imageUris) {
            File file = new File(getRealPathFromURI(uri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
            imageParts.add(part);
        }

        // Gọi API - giả sử bạn có Retrofit interface là reviewApi
        APIRetrofit.getReviewAPIService().uploadReview(reviewRequestBody, imageParts).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FeedBackActivity.this, "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("feedbackUpdated", true);
                    resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
                    setResult(RESULT_OK, resultIntent);

                    finish(); // kết thúc activity và trả về OrderActivity
                } else {
                    Toast.makeText(FeedBackActivity.this, "Lỗi khi gửi đánh giá!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FeedBackActivity.this, "Lỗi mạng hoặc máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });




    }


    // Chuyển đổi Uri thành đường dẫn file thực tế
    private String getRealPathFromURI(Uri uri) {
        return RealPathUtil.getRealPath(this, uri);
    }
    // Function to add the image to the imageContainer
    private void addImageToLayout(Uri imageUri) {
        // Tạo một FrameLayout để chứa ảnh và nút X
        FrameLayout imageContainer = new FrameLayout(this);
        imageContainer.setLayoutParams(new LinearLayout.LayoutParams(150, 150)); // Điều chỉnh kích thước cho ảnh

        // Tạo ImageView để hiển thị ảnh
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(150, 150)); // Điều chỉnh kích thước ảnh
        imageView.setImageURI(imageUri); // Gán ảnh vào ImageView

        // Tạo nút xóa (X)
        ImageView deleteButton = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(30, 30); // Kích thước của nút X
        params.gravity = Gravity.TOP | Gravity.END; // Căn nút X ở góc trên bên phải
        deleteButton.setLayoutParams(params);
        deleteButton.setImageResource(R.drawable.button); // Đảm bảo rằng bạn đã có biểu tượng "X"
        deleteButton.setContentDescription("Delete Image");

        // Sự kiện khi nhấn nút X để xóa ảnh
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUris.remove(imageUri); // Loại bỏ ảnh khỏi danh sách
                LinearLayout imageContainerLayout = findViewById(R.id.imageContainer);
                imageContainerLayout.removeView(imageContainer); // Xóa ảnh khỏi LinearLayout
            }
        });

        // Thêm ảnh và nút X vào FrameLayout
        imageContainer.addView(imageView);
        imageContainer.addView(deleteButton);

        // Cuối cùng, thêm FrameLayout vào LinearLayout (hoặc vào HorizontalScrollView nếu cần)
        LinearLayout imageContainerLayout = findViewById(R.id.imageContainer);
        imageContainerLayout.addView(imageContainer); // Thêm ảnh vào layout
    }

}
