package com.example.retofit2.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class UploadVideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int REQUEST_READ_STORAGE = 100;

    private VideoView videoView;
    private Button btnChooseVideo, btnUploadVideo;
    private Uri videoUri;
    private UploadImageAPI apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video);

        videoView = findViewById(R.id.videoView);
        btnChooseVideo = findViewById(R.id.btnChooseVideo);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);

        apiService = UploadImageClientAPI.getRetrofitInstance().create(UploadImageAPI.class);

        btnChooseVideo.setOnClickListener(v -> openVideoChooser());

        btnUploadVideo.setOnClickListener(v -> {
            if (checkPermission()) {
                uploadVideoToServer();
            } else {
                requestPermission();
            }
        });
    }

    private void openVideoChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

    private boolean checkPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền truy cập!", Toast.LENGTH_SHORT).show();
                uploadVideoToServer();
            } else {
                Toast.makeText(this, "Từ chối quyền truy cập bộ nhớ!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void uploadVideoToServer() {
        if (videoUri != null) {
            String realPath = RealPathUtil.getRealPathVideo(this, videoUri);
            if (realPath == null) {
                Toast.makeText(this, "Không thể lấy đường dẫn video", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(realPath);
            String mimeType = getContentResolver().getType(videoUri);
            String extension = mimeType != null && mimeType.contains("/") ? mimeType.substring(mimeType.lastIndexOf("/") + 1) : "mp4";
            String uniqueFileName = UUID.randomUUID().toString() + "." + extension;

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", uniqueFileName, requestFile);
            String productName = "ProductName";
            Call<ResponseBody> call = apiService.uploadMedia(body, productName);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(UploadVideoActivity.this, "Upload thành công!", Toast.LENGTH_SHORT).show();
                        try {
                            String requestFile = response.body().string();  // Lấy URL ảnh từ response
                            // Log.e(,"taaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");// Gửi URL ảnh về backend
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(UploadVideoActivity.this, "Upload thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(UploadVideoActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
