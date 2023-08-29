package com.tuan1611pupu.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.InvoiceDetail;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.ArrayList;

public class InvoiceDetailAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater = null;
    ArrayList<InvoiceDetail> list;
    {
        list = new ArrayList<>();
    }
    FragmentManager fragmentManager;

    public InvoiceDetailAdapter(Context context, ArrayList<InvoiceDetail> list) {
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
        InvoiceDetail invoiceDetail = list.get(i);
        InvoiceDetailAdapter.ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.invoice_detail_listview_layout, viewGroup, false);
            viewHolder = new InvoiceDetailAdapter.ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.lv_txt_title1);
            viewHolder.price = (TextView) view.findViewById(R.id.lv_txt_price1);
            viewHolder.quantity = view.findViewById(R.id.lv_txt_quantity);
            viewHolder.tongTien = view.findViewById(R.id.tv_tongTien);
            viewHolder.imageView = view.findViewById(R.id.lv_img1);
            view.setTag(viewHolder);
        }else{
            viewHolder = (InvoiceDetailAdapter.ViewHolder) view.getTag();
        }
        viewHolder.title.setText(invoiceDetail.getTitle());
        viewHolder.price.setText(invoiceDetail.getPrice().toString());
        viewHolder.quantity.setText("x"+invoiceDetail.getQuantity());
        Glide.with(context).load(invoiceDetail.getImageUrl()).into(viewHolder.imageView);

        int tongTien;
        tongTien = invoiceDetail.getPrice()*invoiceDetail.getQuantity();
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
