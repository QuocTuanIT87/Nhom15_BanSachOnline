package com.tuan1611pupu.appbansach.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan1611pupu.appbansach.Activity.CheckOutActivity;
import com.tuan1611pupu.appbansach.Activity.MainActivity;
import com.tuan1611pupu.appbansach.Adapter.BookRecAdapter;
import com.tuan1611pupu.appbansach.Adapter.CartItemAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.CartItem;
import com.tuan1611pupu.appbansach.model.Category;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    ArrayList<CartItem> listCart = new ArrayList<>();
    RecyclerView rcListCart;
    CartItemAdapter cartItemAdapter;
    private PreferenceManager preferenceManager;

    private  Button bt_checkOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        preferenceManager = new PreferenceManager(getContext());
        rcListCart = view.findViewById(R.id.rcListCartItem);
        bt_checkOut = view.findViewById(R.id.cart_btnCheckOut);
        cartItemAdapter = new CartItemAdapter(getActivity(),listCart);
        rcListCart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcListCart.setAdapter(cartItemAdapter);
        getCartItem();
        bt_checkOut.setOnClickListener(v->{
            if(listCart.size()>0){
                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
            });
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

                cartItemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {

            }
        });
    }

}
