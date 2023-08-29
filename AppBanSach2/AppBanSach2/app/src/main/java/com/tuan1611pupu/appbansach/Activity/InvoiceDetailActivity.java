package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tuan1611pupu.appbansach.Adapter.InvoiceDetailAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.databinding.ActivityInvoiceDatalBinding;
import com.tuan1611pupu.appbansach.databinding.ActivityInvoiceDetailBinding;
import com.tuan1611pupu.appbansach.model.InvoiceDetail;
import com.tuan1611pupu.appbansach.model.Order;
import com.tuan1611pupu.appbansach.model.User;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceDetailActivity extends AppCompatActivity {
    private ActivityInvoiceDetailBinding binding;
    private Order invoice;
    private ArrayList<InvoiceDetail> listDetail;
    private InvoiceDetailAdapter adapter;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        listDetail = new ArrayList<>();
        adapter = new InvoiceDetailAdapter(getApplicationContext(),listDetail);
        binding.productsList.setAdapter(adapter);
        Intent intent1 = getIntent();
        if (intent1 != null) {
            invoice = (Order) intent1.getSerializableExtra("ORDER_DATA");
        }
        binding.checkoutButton.setOnClickListener(v->{
            Toast.makeText(this, "Đang phát triển, vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
        });
        binding.BackButton.setOnClickListener(v->{
            // check xem quyen neu la user thi cho sua (new statusId ==1) thong tin ca nhan, con admin chi cho xem
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        });
        getInvoiceInfo();
        getDetail(invoice.getInvoiceId());
        getUser(invoice.getMemberId());
    }


    private void getInvoiceInfo() {
        binding.customerAddress.setText(invoice.getAddress());
        binding.customerPhone.setText(invoice.getTel());
    }

    private void getDetail(int invoiceId)
    {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<List<InvoiceDetail>> call = dataService.getDetail(invoiceId,"Bearer " + token);
        call.enqueue(new Callback<List<InvoiceDetail>>() {
            @Override
            public void onResponse(Call<List<InvoiceDetail>> call, Response<List<InvoiceDetail>> response) {
                if(response.isSuccessful())
                {
                    if(listDetail != null) {
                        listDetail.clear(); // xóa các phần tử hiện có
                    }
                    listDetail.addAll(response.body()); // thêm các phần tử mới
                    adapter.notifyDataSetChanged();
                    double totalPrice = 0;
                    for (InvoiceDetail detail : listDetail) {
                        totalPrice += detail.getPrice() * detail.getQuantity();
                    }
                    binding.totalPrice.setText(String.format("Tổng giá trị: %.2f đồng", totalPrice));
                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceDetail>> call, Throwable t) {

            }
        });
    }
    private void getUser(String memberId)
    {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<User> call = dataService.getUser(memberId,"Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {
                    User user = response.body();
                    binding.customerName.setText(user.getFullname());
                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}