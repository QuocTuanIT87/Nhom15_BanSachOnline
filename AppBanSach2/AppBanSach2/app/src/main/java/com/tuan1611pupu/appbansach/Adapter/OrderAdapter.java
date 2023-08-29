package com.tuan1611pupu.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan1611pupu.appbansach.Activity.InvoiceDatalActivity;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.admin.CRUDbookActivity;
import com.tuan1611pupu.appbansach.model.Order;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.HolderOrder> {

    private Context context;
    private ArrayList<Order> orderList;
    private FragmentManager fragmentManager;
    private PreferenceManager preferenceManager;

    public OrderAdapter(Context context, ArrayList<Order> orderList, FragmentManager fm) {
        this.context = context;
        this.orderList = orderList;
        this.fragmentManager = fm;
    }

    @NonNull
    @Override
    public HolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_user, parent, false);
        return new HolderOrder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrder holder, int position) {
        Order order = orderList.get(position);
        String orderId = String.valueOf(order.getInvoiceId());
        String orderTime = order.getAddedDate();
        String orderPhone = order.getTel();
        String orderStatus = String.valueOf(order.getStatusId());

        holder.txtOrder.setText(orderId);
        holder.txtPhone.setText(orderPhone);
        holder.txtStatus.setText(orderStatus);
        preferenceManager = new PreferenceManager(context);

        if (orderStatus.equals("1")){
            holder.txtStatus.setText("Awaiting confirmation");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.primary_color));
        } else if (orderStatus.equals("2")){
            holder.txtStatus.setText("Received");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.second_color));
        } else if (orderStatus.equals("5")){
            holder.txtStatus.setText("Cancelled");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.text_second));
        }else if (orderStatus.equals("3")) {
            holder.txtStatus.setText("Delivery");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.green));
        }else if (orderStatus.equals("4")) {
            holder.txtStatus.setText("Successfully delivered");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.teal_200));
        }


        holder.txtDate.setText(orderTime);
        holder.btnStatus.setOnClickListener(v->{
            if(order.getStatusId() >= 1 && order.getStatusId() < 4)
            {
                int statusId = order.getStatusId() + 1;
                setStatus(order.getInvoiceId(),statusId);
            }else {
                Toast.makeText(context, "Khong the chuyen statusId", Toast.LENGTH_SHORT).show();
            }

        });
        holder.btnInfo.setOnClickListener(v->{
            Intent intent = new Intent(context, InvoiceDatalActivity.class);
            intent.putExtra("ORDER_DATA",order);
            context.startActivity(intent);
        });

    }
    private void setStatus(int invoiceId,int statusId){
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        DataService dataService = APIService.getService();
        Call<Void> call = dataService.updateStatus(invoiceId,statusId,"Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void>response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(context, "Da sua thanh cong", Toast.LENGTH_SHORT).show();
                    Log.d("Wrrrrrr",statusId + "");
                    for (Order order:orderList) {
                        if(order.getInvoiceId() == invoiceId)
                        {
                            order.setStatusId(statusId);
                        }
                    }
                    notifyDataSetChanged();

                }else {
                    try {
                        String errorBody = response.errorBody().string();
                        int code = response.code();
                        // Xử lý thông tin lỗi ở đây0
                        Log.d("Wqqqqqq",errorBody + code);
                        Toast.makeText(context, "So luong sach con lai khong du", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class HolderOrder extends RecyclerView.ViewHolder {
        private TextView txtOrder, txtDate, txtPhone, txtStatus;
        private Button btnInfo,btnStatus;

        public HolderOrder(@NonNull View itemView) {
            super(itemView);

            txtOrder = itemView.findViewById(R.id.txtOrder);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPhone = itemView.findViewById(R.id.txtTel);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnInfo = itemView.findViewById(R.id.btn_invoice);
            btnStatus = itemView.findViewById(R.id.btn_status);
        }
    }
}
