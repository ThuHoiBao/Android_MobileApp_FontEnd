package com.example.retofit2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.retofit2.R;
import com.example.retofit2.api.UploadImageAPI;
import com.example.retofit2.api.retrofit.UploadImageClientAPI;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Button btnChooseImage, btnUploadImage;
    private Uri imageUri;
    private UploadImageAPI apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);

        imageView = findViewById(R.id.imageView);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);

        // Khởi tạo Retrofit API
        apiService = UploadImageClientAPI.getRetrofitInstance().create(UploadImageAPI.class);

        btnChooseImage.setOnClickListener(v -> openImageChooser());
        btnUploadImage.setOnClickListener(v -> uploadImageToServer());
    }

    // Mở thư viện ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);  // Hiển thị ảnh đã chọn
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Upload ảnh lên backend
    private void uploadImageToServer() {
        if (imageUri != null) {
            // Chuyển đổi Uri thành đường dẫn file thực tế
            File file = new File(getRealPathFromURI(imageUri));
            String uniqueImageName = UUID.randomUUID().toString() + ".jpg";  // Tạo tên ảnh duy nhất

            // Tạo MultipartBody.Part từ ảnh
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", uniqueImageName, requestFile);

            // Tên sản phẩm có thể lấy từ EditText hoặc hardcode
            String productName = "ProductName";

            // Gọi API upload ảnh lên backend
            Call<ResponseBody> call = apiService.uploadImage(body, productName);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String imageUrl = response.body().string();  // Lấy URL ảnh từ response
                            sendImageUrlToBackend(imageUrl);
                           // Log.e(,"taaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");// Gửi URL ảnh về backend
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


    // Chuyển đổi Uri thành đường dẫn file thực tế
    private String getRealPathFromURI(Uri uri) {
        return RealPathUtil.getRealPath(this, uri);
    }

    // Gửi URL ảnh về backend để lưu vào cơ sở dữ liệu
    private void sendImageUrlToBackend(String imageUrl) {
        // Gửi imageUrl về backend API để lưu vào database của bạn
    }
}
