package com.tuan1611pupu.appbansach.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tuan1611pupu.appbansach.Activity.LogInActivity;
import com.tuan1611pupu.appbansach.Activity.OrderActivity;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.User;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;


import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends Fragment {
    TextView tvUsername,tvFullname,tvEmail;
    Button btnInvoice,btnLogOut;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        setControl(view);
        preferenceManager = new PreferenceManager(getContext());
        setEvent();
        btnLogOut.setOnClickListener(v->{
            logOut();
        });
        btnInvoice.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), OrderActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void logOut() {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<Void> call = dataService.logOut("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    preferenceManager.clear();
                    Toast.makeText(getContext(), "Dang xuat thanh cong", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LogInActivity.class));

                }
                else {
                    Toast.makeText(getContext(), "Dang xuat that bat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Dang xuat that bat", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setEvent(){
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        String memberId = preferenceManager.getString(Constants.KEY_MEMBER_ID);
        DataService dataService = APIService.getService();
        Call<User> call = dataService.getUser(memberId,"Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {
                    User user = response.body();
                    tvFullname.setText(user.getFullname());
                    tvUsername.setText(user.getUserName());
                    tvEmail.setText(user.getEmail());

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
    private void setControl(View view){
        tvUsername = view.findViewById(R.id.tvUsername);
        tvFullname = view.findViewById(R.id.tvFullname);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnInvoice = view.findViewById(R.id.btn_invoiceMember);
        btnLogOut = view.findViewById(R.id.btn_logOut);
    }
}
