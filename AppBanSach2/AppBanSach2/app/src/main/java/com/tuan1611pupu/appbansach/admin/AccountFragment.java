package com.tuan1611pupu.appbansach.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tuan1611pupu.appbansach.Activity.LogInActivity;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {
    TextView txtName;
    TextView txtPhone;
    LinearLayout groupBtn;
    Button btnLogout;
    Button btnInfor;
    Button btnChangePassword;
    Button btnOrder;
    private PreferenceManager preferenceManager;
    private String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        txtName = view.findViewById(R.id.account_txtUserName);
        txtPhone = view.findViewById(R.id.account_txtUserPhone);
        btnOrder = view.findViewById(R.id.account_btnBills);
        btnLogout = view.findViewById(R.id.account_btnLogOut);
        btnInfor = view.findViewById(R.id.account_btnInformation);
        btnChangePassword = view.findViewById(R.id.account_btnChangePass);
        groupBtn = (LinearLayout) view.findViewById(R.id.account_group_btn);
        preferenceManager = new PreferenceManager(getContext());

        groupBtn.setVisibility(View.VISIBLE);

        btnLogout.setOnClickListener(v->{
            logOut();
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

    private void fillData(String name, String phone) {
        txtName.setText(name);
        txtPhone.setText(phone);
    }
}

