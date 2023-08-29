package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.databinding.ActivityForgotPasswordBinding;
import com.tuan1611pupu.appbansach.model.SignInRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        binding.btnForgot.setOnClickListener(v ->{
            if (binding.txtEmail.getText().toString().trim().isEmpty()) {
                showMessage("Nhập email");
            } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.txtEmail.getText().toString()).matches()){
                showMessage("Nhập đúng định dạng email");
            }else if(binding.txtPass.getText().toString().trim().isEmpty()){
                showMessage("Nhập password");
            }else if(binding.txtPassComfirm.getText().toString().trim().isEmpty()) {
                showMessage("Nhập password xác nhận");
            }else if(!binding.txtPassComfirm.getText().toString().trim().equals(binding.txtPass.getText().toString())) {
                showMessage("Hai password không trùng nhau");
            }else{
                getPass();
            }
        });

    }

    private void getPass() {
        binding.btnForgot.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        DataService dataService = APIService.getService();
        Call<Void> call = dataService.getPassword(binding.txtEmail.getText().toString().trim(),binding.txtPass.getText().toString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    binding.btnForgot.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    showMessage("Vui lòng kiểm tra email");
                    Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    binding.btnForgot.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    showMessage("Sai email");
                    }
                }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.btnForgot.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                showMessage("Thất bại");

            }
        });

    }

    private void showMessage (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}