package com.tuan1611pupu.appbansach.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.CartItem;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private List<CartItem> cartList;
    private Context context;
    private PreferenceManager preferenceManager;


    public CartItemAdapter(Context context,List<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);

        return new CartItemAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        preferenceManager = new PreferenceManager(context);
        CartItem cartItem = cartList.get(position);
        holder.cartTitle.setText(cartItem.getTitle());
        holder.priceCart.setText(cartItem.getPrice().toString());
        holder.soLuong.setText(cartItem.getQuantity().toString());
        Glide.with(context).load(cartItem.getImageUrl()).into(holder.cartImageView);
        holder.btnDeleteCart.setOnClickListener(v->{
            if(cartItem.getCartItemId() != null){
                deleteCart(cartItem.getCartItemId());
            }
        });
        holder.btnTang.setOnClickListener(v->{
            if(cartItem.getCartItemId() != null){
                 int sl =cartItem.getQuantity()+1;
                 cartItem.setQuantity(sl);
                 holder.soLuong.setText(cartItem.getQuantity().toString());
                updateCart(cartItem.getCartItemId(),cartItem.getQuantity());
            }
        });
        holder.btnGiam.setOnClickListener(v->{
            if(cartItem.getCartItemId() != null){
                int sl =cartItem.getQuantity()-1;
                if(sl==0) deleteCart(cartItem.getCartItemId());
                else {
                    cartItem.setQuantity(sl);
                    holder.soLuong.setText(cartItem.getQuantity().toString());
                    updateCart(cartItem.getCartItemId(),cartItem.getQuantity());
                }
            }
        });
    }

    private void updateCart(int cartItemId , int quantity){
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && cartItemId != 0) {

            DataService dataService = APIService.getService();
            Call<Void> call = dataService.updateCartItem(cartItemId,quantity, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
//                        cartList.removeIf(cartItem -> cartItem.getCartItemId() == cartItemId);
//                        notifyDataSetChanged();
//                        // adapter.updateAuthor(response.body());
//                        Toast.makeText(context, "Da xoa thanh cong", Toast.LENGTH_SHORT).show();

                    } else {
                        // Xử lý lỗi khi yêu cầu thất bại
                        try {
                            String errorBody = response.errorBody().string();
                            String code = String.valueOf(response.code());
                            // Xử lý thông tin lỗi ở đây
                            Toast.makeText(context, " that bai", Toast.LENGTH_SHORT).show();
                            Log.d("Akkkk",errorBody+code);
                        } catch (IOException e) {

                            e.printStackTrace();

                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại
                }
            });
        }
    }
    private void deleteCart(Integer cartItemId) {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && cartItemId != 0) {

            DataService dataService = APIService.getService();
            Call<Void> call = dataService.deleteCartItem(cartItemId, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        cartList.removeIf(cartItem -> cartItem.getCartItemId() == cartItemId);
                        notifyDataSetChanged();
                        // adapter.updateAuthor(response.body());
                        Toast.makeText(context, "Da xoa thanh cong", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý lỗi khi yêu cầu thất bại
                        try {
                            String errorBody = response.errorBody().string();
                            String code = String.valueOf(response.code());
                            // Xử lý thông tin lỗi ở đây
                            Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                            Log.d("Akkkk",errorBody+code);
                        } catch (IOException e) {

                            e.printStackTrace();

                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cartImageView;
        TextView cartTitle;
        TextView priceCart,soLuong;
        ImageButton btnDeleteCart;
        Button    btnTang,btnGiam;

        ViewHolder(View itemView) {
            super(itemView);
            cartImageView = itemView.findViewById(R.id.lv_imgCart);
            cartTitle = itemView.findViewById(R.id.lv_txt_titleCart);
            priceCart = itemView.findViewById(R.id.lv_txt_priceCart);
            btnDeleteCart = itemView.findViewById(R.id.btnRemove);
            soLuong = itemView.findViewById(R.id.txtsoluongCart);
            btnGiam = itemView.findViewById(R.id.btn_cart_giamsoluong);
            btnTang = itemView.findViewById(R.id.btn_cart_tangsoloung);
        }
    }
}
