package com.example.retofit2.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import com.example.retofit2.R;
import com.example.retofit2.Toast.CustomToast;
import com.example.retofit2.api.UserAPI;
import com.example.retofit2.api.retrofit.APIRetrofit;
import com.example.retofit2.authentication.AuthManager;
import com.example.retofit2.dto.requestDTO.TokenDTO;
import com.example.retofit2.dto.requestDTO.UserDTO;
import com.example.retofit2.dto.requestDTO.UserLoginDTO;
import com.example.retofit2.utils.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    private boolean passwordShowing = false;
    private EditText usernameET, passwordET;
    private AppCompatButton loginBtn;
    String SiteKey = "";

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    CheckBox selectRemember;
    GoogleApiClient googleApiClient;
    LoadingDialog loadingDialog = new LoadingDialog(Login.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        googleBtn = findViewById(R.id.google_btn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final TextView signUpBtn = findViewById(R.id.signUpBtn);
        final TextView fgPassWordBtn = findViewById(R.id.forgotPassword);
        loginBtn = findViewById(R.id.signInBtn);


        selectRemember = findViewById(R.id.selectRemember);

        // Check if AuthManager is working correctly
        AuthManager authManager = new AuthManager(this);
        authManager.checkLoginStatus();

        passwordIcon.setOnClickListener(new View.OnClickListener(){
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
                passwordET.setSelection(passwordET.length());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String phoneNumber = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if(phoneNumber.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserLoginDTO userLoginDTO = new UserLoginDTO(phoneNumber, password);
                loadingDialog.startLoadingDialog();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadingDialog.dismissDialog();
//                    }
//                }, 2000);
                callLoginApi(userLoginDTO);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, RegisterActivity.class));
            }
        });

        fgPassWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(Login.this)
                .build();
        googleApiClient.connect();

    }

    private void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    sendLoginSocialRequest(account);
                }
            } catch (ApiException e) {
                CustomToast.makeText(Login.this, "Something went wrong!", CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle the click event here
                    }
                }).show();
            }
        }
    }

    private void navigateToSecondActivity(){
        Intent intent = new Intent(Login.this, ProductDetailActivity.class);
        startActivity(intent);
        finish();
    }

    private void callLoginApi(UserLoginDTO userLoginDTO){
        UserAPI userAPI = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        Call<TokenDTO> call = userAPI.login(userLoginDTO);

        call.enqueue(new Callback<TokenDTO>() {
            @Override
            public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {

                if(response.isSuccessful()){
                    loadingDialog.dismissDialog();
                    TokenDTO tokenDTO = response.body();
                    String token = tokenDTO.getToken();
                    Log.d("Token", "Extracted Token: " + token);
                    Long userId = extractUserId(token);
                    Log.d("User Id", "Extracted User Id: " + userId);
                    if(selectRemember.isChecked()){
                        if(userId != null){
                            //Save SharedPreferences
                            SharedPrefManager sharedPrefManager = new SharedPrefManager(Login.this);
                            sharedPrefManager.saveToken(token);
                            sharedPrefManager.saveUserId(userId);
                        }
                    }

                    //Navigative to MainActivity
                    Intent intent = new Intent(Login.this, PaymentActivity.class);
                    CustomToast.makeText(Login.this, "Login successfully!", CustomToast.LONG, CustomToast.SUCCESS, true, Gravity.TOP,350, 100, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle the click event here
                        }
                    }).show();
                    startActivity(intent);
                    finish(); // Finish Login to prevent going back
                } else {
                    loadingDialog.dismissDialog();
                    Toast.makeText(Login.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenDTO> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(Login.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String extractPhoneNumber(String token){
        JWT jwt = new JWT(token);
        return jwt.getSubject();
    }

    public Long extractUserId(String token) {
        try {
            JWT jwt = new JWT(token);

            Claim userIdClaim = jwt.getClaim("userId");

            if (userIdClaim != null && userIdClaim.asLong() != null) {
                return userIdClaim.asLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void sendLoginSocialRequest(GoogleSignInAccount  account){
        UserDTO userDTO = new UserDTO();
        userDTO.setGoogle_account_id(account.getId());
        userDTO.setEmail(account.getEmail());
        userDTO.setFullname(account.getDisplayName());
        userDTO.setProfile_image(account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");

        UserAPI apiService = APIRetrofit.getRetrofitInstance().create(UserAPI.class);
        Call<TokenDTO> call = apiService.loginWithGoogle(userDTO);
        call.enqueue(new Callback<TokenDTO>() {
            @Override
            public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                if(response.isSuccessful() && response.body() != null){
                    String token = response.body().getToken();
                    Long userId = extractUserId(token);
                    SharedPrefManager prefManager = new SharedPrefManager(Login.this);
                    prefManager.saveToken(token);
                    prefManager.saveUserId(userId);
                    Log.d("User Id", "Extracted User Id: " + userId);
                    CustomToast.makeText(Login.this, "Google login successful!", CustomToast.LONG, CustomToast.SUCCESS, true, Gravity.TOP,350, 100, null).show();
                    navigateToSecondActivity();
                } else {
                    CustomToast.makeText(Login.this, "Google login failed!", CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, null).show();
                }
            }

            @Override
            public void onFailure(Call<TokenDTO> call, Throwable t) {
                CustomToast.makeText(Login.this, "Network error: " + t.getMessage(), CustomToast.LONG, CustomToast.ERROR, true, Gravity.TOP,350, 100, null).show();
            }
        });
    }
}
