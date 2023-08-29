package com.tuan1611pupu.appbansach.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan1611pupu.appbansach.Adapter.OrderAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
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

public class OrderListFragment extends Fragment {
    private RecyclerView ordersRv;
    private ArrayList<Order> orderList;
    private OrderAdapter orderAdapter;
    private Spinner spinnerStatus;
    private Map<String,String> statusMap= new HashMap<>();
    private ArrayList<String> statusName = new ArrayList<>();
    private ArrayAdapter statusAdapter;
    FragmentManager fragmentManager;
    private TextView tv_orderNull;
    private int statusId;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order_list, container, false);
        ordersRv = view.findViewById(R.id.ordersRv);
        spinnerStatus = view.findViewById(R.id.status_spinner);
        tv_orderNull = view.findViewById(R.id.tv_oderNull);
        fragmentManager = getParentFragmentManager();
        preferenceManager = new PreferenceManager(getContext());
        if(statusName.size() == 0){
            statusName.add("All");
            statusMap.put("All","0");
            statusName.add("Awaiting confirmation");
            statusMap.put("Awaiting confirmation","1");
            statusName.add("Received");
            statusMap.put("Received","2");
            statusName.add("Delivery");
            statusMap.put("Delivery","3");
            statusName.add("Successfully delivered");
            statusMap.put("Successfully delivered","4");
            statusName.add("Cancelled");
            statusMap.put("Cancelled","5");
        }
        statusAdapter = new ArrayAdapter(getContext(),R.layout.style_spinner,statusName);
        spinnerStatus.setAdapter(statusAdapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String statusName = spinnerStatus.getItemAtPosition(position).toString();
                String statusId1 = statusMap.get(statusName);
                // Now you have the authorId corresponding to the selected authorName
                statusId = Integer.parseInt(statusId1);
                if(statusId == 0){
                    loadOrders();
                }else {
                    loadOrderOfStatus(statusId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        return view;
    }
    private void loadOrders() {
        orderList = new ArrayList<>();
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<List<Order>> call = dataService.getAllInvoice("Bearer " + token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful())
                {
                    if(orderList != null) {
                        orderList.clear(); // xóa các phần tử hiện có
                    }
                    orderList.addAll(response.body()); // thêm các phần tử mới
                    orderAdapter = new OrderAdapter(getContext(),orderList,fragmentManager);
                    ordersRv.setAdapter(orderAdapter);
                    if(orderList.size() == 0)
                    {
                        tv_orderNull.setVisibility(View.VISIBLE);
                    }else {
                        tv_orderNull.setVisibility(View.INVISIBLE);
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

    private void loadOrderOfStatus(int statusId){
        orderList = new ArrayList<>();
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<List<Order>> call = dataService.getInvoiceOfStatus(statusId,"Bearer " + token);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful())
                {
                    if(orderList != null) {
                        orderList.clear(); // xóa các phần tử hiện có
                    }
                    orderList.addAll(response.body()); // thêm các phần tử mới
                    orderAdapter = new OrderAdapter(getContext(),orderList,fragmentManager);
                    ordersRv.setAdapter(orderAdapter);
                    if(orderList.size() == 0)
                    {
                        tv_orderNull.setVisibility(View.VISIBLE);
                    }else {
                        tv_orderNull.setVisibility(View.INVISIBLE);
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