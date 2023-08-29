package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.databinding.ActivitySignUpBinding;
import com.tuan1611pupu.appbansach.model.SignInRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });
        binding.btnRegister.setOnClickListener(v ->{
            if(binding.txtFullname.getText().toString().trim().isEmpty()){
                showMessage("Nhập họ và tên");
            } else if (binding.txtEmail.getText().toString().trim().isEmpty()) {
                showMessage("Nhập email");
            } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.txtEmail.getText().toString()).matches()){
                showMessage("Nhập đúng định dạng email");
            }else if(binding.txtUsername.getText().toString().isEmpty()) {
                showMessage("Nhập username");
            }else if(binding.txtPass.getText().toString().trim().isEmpty()){
                showMessage("Nhập password");
            }else if(binding.txtPassComfirm.getText().toString().trim().isEmpty()) {
                showMessage("Nhập password xác nhận");
            }else if(!binding.txtPassComfirm.getText().toString().trim().equals(binding.txtPass.getText().toString())) {
                showMessage("Hai password không trùng nhau");
            }else{
                signUp();
            }
        });

    }

    public void dangKy(SignInRequest signInRequest){
        binding.btnRegister.setVisibility(View.GONE);
        binding.progressBarRegister.setVisibility(View.VISIBLE);
        DataService dataService = APIService.getService();
        Call<SignInRequest> userCall = dataService.createUser("User", signInRequest);
        userCall.enqueue(new Callback<SignInRequest>() {
            @Override
            public void onResponse(Call<SignInRequest> call, Response<SignInRequest> response) {
                if(response.isSuccessful())
                {
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    binding.progressBarRegister.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Vui lòng xác nhận tài khoản qua email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    intent.putExtra("signInRequest",signInRequest);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    binding.progressBarRegister.setVisibility(View.GONE);
                    int code = response.code();
                    if(code == 403)
                    {
                        Toast.makeText(SignUpActivity.this, "Email đã đăng ký vui lòng chọn email khác", Toast.LENGTH_SHORT).show();
                    }else if(code == 500)
                    {
                        Toast.makeText(SignUpActivity.this, "Username đã được sử dụng vui lòng chọn Username khác", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignInRequest> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t+"", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signUp() {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setFullName(binding.txtFullname.getText().toString().trim());
        signInRequest.setUserName(binding.txtUsername.getText().toString().trim());
        signInRequest.setEmail(binding.txtEmail.getText().toString().trim());
        signInRequest.setPassword(binding.txtPass.getText().toString());
        dangKy(signInRequest);


    }

    private void showMessage (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}