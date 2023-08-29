package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.tuan1611pupu.appbansach.Adapter.InvoiceAdapter;
import com.tuan1611pupu.appbansach.Adapter.InvoiceDetailAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.databinding.ActivityCheckOutBinding;
import com.tuan1611pupu.appbansach.model.CartItem;
import com.tuan1611pupu.appbansach.model.Invoice;
import com.tuan1611pupu.appbansach.model.InvoiceDetail;
import com.tuan1611pupu.appbansach.model.Order;
import com.tuan1611pupu.appbansach.model.User;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CheckOutActivity extends AppCompatActivity {
    private Order invoice;
    private ArrayList<CartItem> listCart;
    private InvoiceAdapter adapter;
    private PreferenceManager preferenceManager;
    private ActivityCheckOutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        listCart = new ArrayList<>();
        adapter = new InvoiceAdapter(getApplicationContext(),listCart);
        binding.productsList.setAdapter(adapter);
        Intent intent1 = getIntent();
        if (intent1 != null) {
            invoice = (Order) intent1.getSerializableExtra("ORDER_DATA");
        }
        binding.xacNhanButton.setOnClickListener(v->{
            if(binding.customerName.getText().toString().trim().isEmpty()){
                showMessage("Nhập họ và tên");
            } else if (binding.customerEmail.getText().toString().trim().isEmpty()) {
                showMessage("Nhập email");
            } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.customerEmail.getText().toString()).matches()){
                showMessage("Nhập đúng định dạng email");
            }else if(binding.customerAddress.getText().toString().isEmpty()) {
                showMessage("Nhập địa chỉ");
            }else if(binding.customerPhone.getText().toString().trim().isEmpty()){
                showMessage("Nhập Số điện thoại ");
            }else{
                addInvoice();

            }
            // check xem quyen neu la user thi cho sua (new statusId ==1) thong tin ca nhan, con admin chi cho xem

        });
        getCartItem();

        getUser();
    }
    private  void addInvoice(){
        Invoice invoice = new Invoice();
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        String memberId = preferenceManager.getString(Constants.KEY_MEMBER_ID);
        DataService dataService = APIService.getService();
        invoice.setMemberId(memberId);
        invoice.setEmail(binding.customerEmail.getText().toString().trim());
        invoice.setAddress(binding.customerAddress.getText().toString());
        invoice.setTel(binding.customerPhone.getText().toString().trim());
        if (token != null) {
            Call<Invoice> call = dataService.addInvoice(invoice, "Bearer " + token);
            call.enqueue(new Callback<Invoice>() {
                @Override
                public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                    if (response.isSuccessful()) {
                        // adapter.updateAuthor(response.body());
                        Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Xử lý lỗi khi yêu cầu thất bại
                        try {
                            String errorBody = response.errorBody().string();
                            String code = String.valueOf(response.code());
                            // Xử lý thông tin lỗi ở đây
                            Toast.makeText(getApplicationContext(), " that bai", Toast.LENGTH_SHORT).show();
                            Log.d("Akkkk",errorBody+code);
                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (java.io.IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Invoice> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại
                }
            });
        }

    }
    private void getCartItem() {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        String memberId = preferenceManager.getString(Constants.KEY_MEMBER_ID);
        DataService dataService = APIService.getService();
        Call<List<CartItem>> call = dataService.getCartItem(memberId,"Bearer " + token);
        call.enqueue(new Callback<List<CartItem>>() {
            @Override
                public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                    if(listCart != null) {
                        listCart.clear(); // xóa các phần tử hiện c
                    }
                    listCart.addAll(response.body());// thêm các phần tử mới

                int tongtien = 0;
                for(CartItem  item : listCart){
                       tongtien+= item.getPrice()*item.getQuantity();
                        }
                binding.totalPrice.setText("Tổng tiền :"+tongtien+" VND");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

            }
        });
    }
    private void getUser()
    {
        String memberId =preferenceManager.getString(Constants.KEY_MEMBER_ID);
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
                    binding.customerEmail.setText(user.getEmail());
                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    private void showMessage (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}