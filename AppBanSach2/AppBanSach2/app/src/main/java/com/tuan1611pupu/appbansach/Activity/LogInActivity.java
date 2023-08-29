package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.databinding.ActivityLogInBinding;
import com.tuan1611pupu.appbansach.model.LoginRequest;
import com.tuan1611pupu.appbansach.model.SignInRequest;
import com.tuan1611pupu.appbansach.model.UserExprech;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.CookieHandler;
import java.net.CookieManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;
    private PreferenceManager preferenceManager;
    private SignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        Intent intent1 = getIntent();
        signInRequest = (SignInRequest) intent1.getSerializableExtra("signInRequest");
        if(signInRequest != null){
            binding.loginTxtUsername.setText(signInRequest.getUserName());
            binding.loginTxtPass.setText(signInRequest.getPassword());
        }


        binding.txtQuenMatKhau.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(intent);
        });
        binding.loginBtnRegister.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        binding.loginBtnGetcode.setOnClickListener(v ->{
            if(binding.loginTxtUsername.getText().toString().trim().isEmpty()) {
                showMessage("nhap username");
            }else if(binding.loginTxtPass.getText().toString().trim().isEmpty()){
                showMessage("nhap password");
            }
            else {
                getCode();
            }
        });

        binding.loginBtnLogin.setOnClickListener(v -> {
            if(binding.loginTxtUsername.getText().toString().trim().isEmpty()){
                showMessage("nhap username");
            }else if(binding.loginTxtcode.getText().toString().trim().isEmpty()){
                showMessage("vui long nhap code");
            }else if(binding.loginTxtPass.getText().toString().trim().isEmpty()){
                showMessage("nhap password");
            }else {
                logIn();
            }
        });

    }

    private void logIn() {
        binding.progressBarLogIn.setVisibility(View.VISIBLE);
        binding.loginBtnLogin.setVisibility(View.GONE);
        String code = binding.loginTxtcode.getText().toString().trim();
        String username = binding.loginTxtUsername.getText().toString().trim();
        DataService dataService = APIService.getService();
        Call<UserExprech> call = dataService.login2FA(code,username);
        call.enqueue(new Callback<UserExprech>() {
            @Override
            public void onResponse(Call<UserExprech> call, Response<UserExprech> response) {
                if (response.isSuccessful()) {
                    binding.progressBarLogIn.setVisibility(View.GONE);
                    binding.loginBtnLogin.setVisibility(View.VISIBLE);
                    try {
                        UserExprech userExprech = response.body();
                        String token = userExprech.getToken();
                        String memberId = userExprech.getMemberId();
                        preferenceManager.putString(Constants.KEY_TOKEN,token);
                        preferenceManager.putString(Constants.KEY_MEMBER_ID,memberId);
                        Log.d("arrrrr",token);
                        Claims claims = Jwts.parser()
                                .setSigningKey("JWTAuthenticationHIGHsecuredPasswordVVVp1OH7Xzyr".getBytes("UTF8"))
                                .parseClaimsJws(token)
                                .getBody();

                        String roles = (String) claims.get("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
                        if (roles.contains("Admin")) {
                            // Cho phép truy cập các tính năng dành cho quản trị viên
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putBoolean(Constants.KEY_IS_ADMIN,true);
                            Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // Cho phép truy cập các tính năng dành cho người dùng
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);


                        }

                    } catch (IOException e) {

                    }
                } else {
                    binding.progressBarLogIn.setVisibility(View.GONE);
                    binding.loginBtnLogin.setVisibility(View.VISIBLE);
                    showMessage("Vui lòng kiểm tra lại");
                    try {
                        String errorBody = response.errorBody().string();
                        // Xử lý thông tin lỗi ở đây0
                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<UserExprech> call, Throwable t) {
                // Xử lý lỗi kết nối
            }
        });

    }

    private void showMessage (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void getCode(){
        binding.loginBtnGetcode.setVisibility(View.GONE);
        binding.progressBarCode.setVisibility(View.VISIBLE);
        LoginRequest request = new LoginRequest();
        request.setUsername(binding.loginTxtUsername.getText().toString().trim());
        request.setPassword(binding.loginTxtPass.getText().toString().trim());
        DataService dataService = APIService.getService();
        Call<Void> call = dataService.login(request);

        call.enqueue(new Callback<Void>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Xử lý phản hồi từ API khi yêu cầu thành công
                if(response.isSuccessful())
                {
                    binding.loginBtnGetcode.setVisibility(View.VISIBLE);
                    binding.progressBarCode.setVisibility(View.GONE);
                    showMessage ("Đã gửi code đến email liên kết tài khoản");
                }
                else {
                    binding.loginBtnGetcode.setVisibility(View.VISIBLE);
                    binding.progressBarCode.setVisibility(View.GONE);
                    showMessage("Sai tài khoản hoặc mật khẩu");
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi yêu cầu thất bại
                showMessage ("Đã gửi code đến email liên kết tài khoản thất bại");
            }
        });
    }


}