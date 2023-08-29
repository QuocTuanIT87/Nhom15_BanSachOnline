package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tuan1611pupu.appbansach.Adapter.InvoiceAdapter;
import com.tuan1611pupu.appbansach.Adapter.OrderAdapter;
import com.tuan1611pupu.appbansach.Adapter.OrderUserAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.databinding.ActivityCheckOutBinding;
import com.tuan1611pupu.appbansach.databinding.ActivityOrderBinding;
import com.tuan1611pupu.appbansach.model.Order;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private ActivityOrderBinding binding;
    private ArrayList<Order> orderList;
    private OrderUserAdapter orderAdapter;
    private Spinner spinnerStatus;
    private Map<String,String> statusMap= new HashMap<>();
    private ArrayList<String> statusName = new ArrayList<>();
    private ArrayAdapter statusAdapter;
    FragmentManager fragmentManager;
    private TextView tv_orderNull;
    private int statusId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        orderList = new ArrayList<>();
        orderAdapter = new OrderUserAdapter(this,orderList);
        binding.ordersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.ordersRv.setAdapter(orderAdapter);
        loadOrders();
    }

    private void loadOrders() {
        String memberId = preferenceManager.getString(Constants.KEY_MEMBER_ID);
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<List<Order>> call = dataService.getInvoiceUser(memberId,"Bearer " + token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful())
                {
                    if(orderList != null) {
                        orderList.clear(); // xóa các phần tử hiện có
                    }
                    orderList.addAll(response.body());
                    // thêm các phần tử mới
                    orderAdapter.notifyDataSetChanged();
                    Log.d("loi o day ",orderList.size()+" ");
                    if(orderList.size() == 0)
                    {
                        binding.tvOderNull.setVisibility(View.VISIBLE);
                    }else {
                        binding.tvOderNull.setVisibility(View.INVISIBLE);
                    }

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });

    }

}