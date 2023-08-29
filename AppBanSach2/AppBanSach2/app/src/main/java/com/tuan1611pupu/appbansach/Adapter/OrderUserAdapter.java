package com.tuan1611pupu.appbansach.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.tuan1611pupu.appbansach.Activity.InvoiceDetailActivity;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.InvoiceDetail;
import com.tuan1611pupu.appbansach.model.Order;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderUserAdapter extends RecyclerView.Adapter<OrderUserAdapter.HolderOrderUser> {

    private Context context;
    private ArrayList<Order> orderList;
    LayoutInflater inflater = null;
    private PreferenceManager preferenceManager;

    public OrderUserAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public OrderUserAdapter.HolderOrderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order, parent, false);
        return new OrderUserAdapter.HolderOrderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderUserAdapter.HolderOrderUser holder, int position) {
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

        if(order.getStatusId()!=1) holder.btnStatus.setVisibility(View.INVISIBLE);
        holder.txtDate.setText(orderTime);
        holder.btnStatus.setOnClickListener(v->{
            if(order.getInvoiceId() != null) deleteOrder(order.getInvoiceId());

        });
        holder.btnInfo.setOnClickListener(v->{
            Intent intent = new Intent(context, InvoiceDetailActivity.class);
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
            public void onResponse(Call<Void> call, Response<Void> response) {
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


    private void deleteOrder(int invoiceId){
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && invoiceId != 0) {

            DataService dataService = APIService.getService();
            Call<Void> call = dataService.deleteInvoice(invoiceId, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        orderList.removeIf(Order -> Order.getInvoiceId() == invoiceId);
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
        return orderList.size();
    }

    class HolderOrderUser extends RecyclerView.ViewHolder {
        private TextView txtOrder, txtDate, txtPhone, txtStatus;
        private Button btnInfo,btnStatus;

        public HolderOrderUser(@NonNull View itemView) {
            super(itemView);

            txtOrder = itemView.findViewById(R.id.txtOrder_user);
            txtDate = itemView.findViewById(R.id.txtDate_user);
            txtPhone = itemView.findViewById(R.id.txtTel_user);
            txtStatus = itemView.findViewById(R.id.txtStatus_user);
            btnInfo = itemView.findViewById(R.id.btn_invoice_user);
            btnStatus = itemView.findViewById(R.id.btn_status_user);
        }
    }
}
