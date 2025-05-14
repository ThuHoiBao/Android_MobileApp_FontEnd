package com.example.retofit2.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.retofit2.R;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.UserRequestDTO;
import com.example.retofit2.dto.responseDTO.UserResponseDTO;
import com.example.retofit2.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText dtDateOfBirth;  // Reference tới EditText của Ngày sinh
    private EditText txtName;
    private TextView txtEmail,txtPhone, txtLogout;
    private ImageView backImage;
    CircleImageView imgProfile;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView txtUpdate;// Reference tới EditText của Tên
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Long userId = getIntent().getLongExtra("userId", -1);
        int userIdInt = userId.intValue();
        dtDateOfBirth = findViewById(R.id.dtDateOfBirth);  // Lấy đối tượng EditText


        // Cài đặt sự kiện khi người dùng nhấn vào EditText
        dtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        txtName = findViewById(R.id.txtName);  // Lấy đối tượng EditText của Tên
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone=findViewById(R.id.txtPhone);
        imgProfile=findViewById(R.id.imgProfile);
        txtLogout = findViewById(R.id.btnLogout);
        txtLogout.setOnClickListener(v -> {
            SharedPrefManager sharedPrefManager = new SharedPrefManager(ProfileActivity.this);
            sharedPrefManager.clearSharedPreferences();
            Intent intent = new Intent(ProfileActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
        // Cài đặt sự kiện khi người dùng nhấn vào EditText
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNameInputDialog();
            }
        });
        loadUserProfile(userIdInt);
        txtUpdate= findViewById(R.id.txtUpdate);
        txtUpdate.setOnClickListener(v -> uploadProfile(userIdInt));

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
        imgProfile.setOnClickListener(v -> openImageChooser());



    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgProfile.setImageBitmap(bitmap);  // Hiển thị ảnh đã chọn
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadProfile(int userIdInt) {
        // Thu thập thông tin từ các trường trong layout
        String fullName = txtName.getText().toString().trim();
        String dateOfBirth = dtDateOfBirth.getText().toString().trim();


        // Kiểm tra và tạo đối tượng UserResponseDTO
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(userIdInt);
        userResponseDTO.setFullName(fullName);
        userResponseDTO.setDateOfBirth(parseDate(dateOfBirth));  // Chuyển đổi ngày sinh nếu cần

        // Chuyển đổi UserResponseDTO thành JSON
        Gson gson = new Gson();
        String userJsonString = gson.toJson(userResponseDTO);
        RequestBody userJson = RequestBody.create(MediaType.parse("application/json"), userJsonString);

        // Kiểm tra nếu có ảnh đại diện
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            // Chuyển đổi Uri thành đường dẫn file thực tế
            File file = new File(getRealPathFromURI(imageUri));
            String uniqueImageName = UUID.randomUUID().toString() + ".jpg";  // Tạo tên ảnh duy nhất

            // Tạo MultipartBody.Part từ ảnh
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
            imagePart = MultipartBody.Part.createFormData("imgProfile", uniqueImageName, requestFile);
        }

        // Gọi API để cập nhật hồ sơ
        UserAPI userAPI = APIRetrofit.getUserApiService();
        Call<ResponseBody> call = userAPI.updateProfileUser(userJson, imagePart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();  // Lấy dữ liệu phản hồi từ server
                        Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // Chuyển đổi chuỗi ngày sinh thành Date
    private Date parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;  // Trả về null nếu không thể chuyển đổi ngày
    }
    private String getRealPathFromURI(Uri uri) {
        return RealPathUtil.getRealPath(this, uri);
    }
    private void showNameInputDialog() {
        // Tạo EditText để người dùng nhập tên
        final EditText input = new EditText(ProfileActivity.this);
        input.setHint("Nhập tên mới");

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Cập nhật tên")
                .setView(input)
                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString();
                        if (!newName.isEmpty()) {
                            txtName.setText(newName);  // Cập nhật tên mới vào txtName
                            Toast.makeText(ProfileActivity.this, "Tên đã được cập nhật", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .create()
                .show();
    }

    // Hàm để mở DatePickerDialog
    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ProfileActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Cập nhật giá trị ngày sinh sau khi chọn
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth);

                    // Định dạng ngày tháng
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dtDateOfBirth.setText(sdf.format(selectedDate.getTime()));  // Hiển thị ngày đã chọn
                },
                year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    private void loadUserProfile(int userId) {
        // Lấy dịch vụ API
        UserAPI userAPI = APIRetrofit.getUserApiService();

        // Thực hiện cuộc gọi API
        Call<UserRequestDTO> call = userAPI.getUserProfile(userId);

        call.enqueue(new Callback<UserRequestDTO>() {
            @Override
            public void onResponse(Call<UserRequestDTO> call, Response<UserRequestDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserRequestDTO user = response.body();

                    // Kiểm tra nếu fullName không có giá trị, đặt "Thiết lập ngay"
                    String fullName = user.getFullName();
                    if (fullName == null || fullName.isEmpty()) {
                        txtName.setText("Thiết lập ngay");
                    } else {
                        txtName.setText(fullName);  // Gán tên người dùng
                    }

                    // Kiểm tra nếu email không có giá trị, đặt "Thiết lập ngay"
                    String email = user.getEmail();
                    if (email == null || email.isEmpty()) {
                        txtEmail.setText("Thiết lập ngay");
                    } else {
                        txtEmail.setText(email);  // Gán email người dùng
                    }

                    // Kiểm tra nếu phoneNumber không có giá trị, đặt "Thiết lập ngay"
                    String phoneNumber = user.getPhoneNumber();
                    if (phoneNumber == null || phoneNumber.isEmpty()) {
                        txtPhone.setText("Thiết lập ngay");
                    } else {
                        txtPhone.setText(phoneNumber);  // Gán số điện thoại người dùng
                    }

                    // Cập nhật ngày sinh nếu có dữ liệu
                    if (user.getDateOfBirth() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        dtDateOfBirth.setText(sdf.format(user.getDateOfBirth()));
                    } else {
                        dtDateOfBirth.setText("Thiết lập ngay");  // Nếu không có ngày sinh, hiển thị "Thiết lập ngay"
                    }

                    // Kiểm tra URL ảnh trước khi sử dụng Glide
                    String imageUrl = user.getProfileImage();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this)
                                .load(imageUrl)  // Tải ảnh từ URL
                                .into(imgProfile);  // Đặt ảnh vào CircleImageView
                    } else {
                        // Nếu không có URL ảnh, sử dụng ảnh mặc định
                        Glide.with(ProfileActivity.this)
                                .load(R.drawable.userprofile)  // Dùng ảnh mặc định nếu không có URL
                                .into(imgProfile);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Lỗi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRequestDTO> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}