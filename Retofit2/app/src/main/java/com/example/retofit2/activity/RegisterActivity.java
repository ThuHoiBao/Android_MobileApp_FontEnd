package com.example.retofit2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.example.retofit2.R;
import com.example.retofit2.Toast.CustomToast;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.dto.requestDTO.UserDTO;
import com.example.retofit2.utils.EmailValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private boolean isEmailAvailable = false;
    private boolean isPhoneAvailable = false;
    private boolean isCheckingEmailDone = false;
    private boolean isCheckingPhoneDone = false;
    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    private EditText fullNamET, phoneNumberET, emailET, passwordET, conPasswordET;
    private AppCompatButton btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        fullNamET = findViewById(R.id.fullNameET);
        emailET = findViewById(R.id.emailET);
        phoneNumberET = findViewById(R.id.phoneET);
        passwordET = findViewById(R.id.passwordET);
        conPasswordET = findViewById(R.id.conPasswordET);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final ImageView conPasswordIcon = findViewById(R.id.conPasswordIcon);
        btnSignUp = findViewById(R.id.signUpBtn);
        final TextView signInBtn = findViewById(R.id.signInBtn);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if password is showing or not
                if(passwordShowing){
                    passwordShowing = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_show);
                } else {
                    passwordShowing = true;
                    passwordET.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.password_hide);
                }
                //move the cursor at last of the text
                passwordET.setSelection(passwordET.length());
            }
        });

        conPasswordIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Checking if password is showing or not
                if(conPasswordShowing){
                    conPasswordShowing = false;
                    conPasswordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.password_show);
                } else {
                    conPasswordShowing = true;
                    conPasswordET.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.password_hide);
                }
                //move the cursor at last of the text
                conPasswordET.setSelection(conPasswordET.length());
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName =  fullNamET.getText().toString().trim();
                final String phone = phoneNumberET.getText().toString().trim();
                final String email = emailET.getText().toString().trim();
                final String password = passwordET.getText().toString().trim();
                final String confirmPassword = conPasswordET.getText().toString().trim();

                if(fullName.isEmpty()){
                    CustomToast.makeText(RegisterActivity.this, "Fullname is not empty!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    return;
                }


                if(phone.isEmpty()){
                    CustomToast.makeText(RegisterActivity.this, "Phone is not empty!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    return;
                }



                if(email.isEmpty()){
                    CustomToast.makeText(RegisterActivity.this, "Email is not empty!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }

                    }).show();
                    return;
                } else if(!EmailValidator.isValidEmail(email)){
                    CustomToast.makeText(RegisterActivity.this, "Email is not in correct format!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                    return;
                }

                if(password.isEmpty() || confirmPassword.isEmpty()){
                    CustomToast.makeText(RegisterActivity.this,
                            "Password or ConfirmPassWord is not empty!",
                            CustomToast.LONG, CustomToast.WARNING,
                            true, Gravity.TOP,350, 100,
                            new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                    return;
                } else if(!password.equals(confirmPassword)){
                    CustomToast.makeText(RegisterActivity.this,
                            "Password and password confirmation must be the same",
                            CustomToast.LONG, CustomToast.WARNING, true,
                            Gravity.TOP,350, 100, new View.OnClickListener() {
                          @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                    return;
                }

                UserDTO userDTO = new UserDTO();
                userDTO.setFullname(fullName);
                userDTO.setPhone_number(phone);
                userDTO.setEmail(email);
                userDTO.setPassword(password);
                userDTO.setRetype_password(confirmPassword);
                userDTO.setFacebook_account_id("");
                userDTO.setGoogle_account_id("");
                userDTO.setRole_id(2);

                // Reset flags
                isCheckingEmailDone = false;
                isCheckingPhoneDone = false;

                callApiCheckEmail(userDTO);
                callApiCheckPhone(userDTO);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(RegisterActivity.this, Login.class));
            }
        });

    }

    private void callApiCheckEmail(UserDTO userDTO) {
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        userAPI.verifyEmail(userDTO.getEmail()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isCheckingEmailDone = true;
                isEmailAvailable = response.isSuccessful() && !"exists".equalsIgnoreCase(response.body());
                checkValidationDone(userDTO);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isCheckingEmailDone = true;
                isEmailAvailable = false;
                checkValidationDone(userDTO);
            }
        });
    }


    private void callApiCheckPhone(UserDTO userDTO) {
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        userAPI.verifyPhone(userDTO.getPhone_number()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isCheckingPhoneDone = true;
                isPhoneAvailable = response.isSuccessful() && !"exists".equalsIgnoreCase(response.body());
                checkValidationDone(userDTO);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isCheckingPhoneDone = true;
                isPhoneAvailable = false;
                checkValidationDone(userDTO);
            }
        });
    }


    private void checkValidationDone(UserDTO userDTO) {
        if (isCheckingEmailDone && isCheckingPhoneDone) {
            checkAndRegister(userDTO);
        }
    }

    private void checkAndRegister(UserDTO userDTO) {
        if (!isEmailAvailable) {
            CustomToast.makeText(RegisterActivity.this, "Email đã tồn tại!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event here
                }
            }).show();
            return;
        }

        if (!isPhoneAvailable) {
            CustomToast.makeText(RegisterActivity.this, "Phone đã tồn tại!", CustomToast.LONG, CustomToast.WARNING, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event here
                }
            }).show();
            return;
        }

        Intent intent = new Intent(RegisterActivity.this, OTPVerification.class);
        intent.putExtra("email", userDTO.getEmail());
        intent.putExtra("flag", "Register");
        intent.putExtra("userDTO", userDTO);
        startActivity(intent);
        finish();
    }



}