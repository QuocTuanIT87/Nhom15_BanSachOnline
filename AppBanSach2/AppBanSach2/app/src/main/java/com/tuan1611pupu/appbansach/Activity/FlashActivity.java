package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.databinding.ActivityFlashBinding;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashActivity extends AppCompatActivity {
    private ActivityFlashBinding binding;
    private static int TIME_OUT = 4000; //Time to launch the another activity
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check login state
                if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                    testToken(preferenceManager.getString(Constants.KEY_TOKEN));
                }else {
                    Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },TIME_OUT);

    }
    private void testToken(String token){
        DataService dataService = APIService.getService();
        Call<Void> call = dataService.testToken("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Xử lý phản hồi từ API khi yêu cầu thành công
                if(response.isSuccessful())
                {
                    if(preferenceManager.getBoolean(Constants.KEY_IS_ADMIN))
                    {
                        Intent intent = new Intent(getApplicationContext(),AdminMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        // user
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Log.d("Xkkkk","o day nay");
                    // het han
                    preferenceManager.clear();
                    Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi yêu cầu thất bại
                Log.d("Xkkkktt","o day nay");
            }
        });
    }
}