package com.tuan1611pupu.appbansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.CartItem;
import com.tuan1611pupu.appbansach.model.InvoiceDetail;

import java.util.ArrayList;

public class InvoiceAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater = null;
    ArrayList<CartItem> list;
    {
        list = new ArrayList<>();
    }
    FragmentManager fragmentManager;

    public InvoiceAdapter(Context context, ArrayList<CartItem> list) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CartItem CartItem = list.get(i);
        InvoiceAdapter.ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.invoice_detail_listview_layout, viewGroup, false);
            viewHolder = new InvoiceAdapter.ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.lv_txt_title1);
            viewHolder.price = (TextView) view.findViewById(R.id.lv_txt_price1);
            viewHolder.quantity = view.findViewById(R.id.lv_txt_quantity);
            viewHolder.tongTien = view.findViewById(R.id.tv_tongTien);
            viewHolder.imageView = view.findViewById(R.id.lv_img1);
            view.setTag(viewHolder);
        }else{
            viewHolder = (InvoiceAdapter.ViewHolder) view.getTag();
        }
        viewHolder.title.setText(CartItem.getTitle());
        viewHolder.price.setText(CartItem.getPrice().toString());
        viewHolder.quantity.setText("x"+CartItem.getQuantity());
        Glide.with(context).load(CartItem.getImageUrl()).into(viewHolder.imageView);

        int tongTien;
        tongTien = CartItem.getPrice()*CartItem.getQuantity();
        viewHolder.tongTien.setText(tongTien+"");



        return view;
    }

    private class ViewHolder{
        TextView title;
        TextView price;
        TextView quantity;
        TextView tongTien;
        ImageView imageView;

    }
}