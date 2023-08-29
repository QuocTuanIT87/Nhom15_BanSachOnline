package com.tuan1611pupu.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.Activity.SignUpActivity;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.admin.CRUDbookActivity;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.AuthorList;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.SignInRequest;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListViewAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater = null;
    private PreferenceManager preferenceManager;
    private Author author = new Author();
    ArrayList<Book> list;
    {
        list = new ArrayList<>();
    }
    FragmentManager fragmentManager;
    public BookListViewAdapter(Context context, ArrayList<Book> list, FragmentManager fragmentManager) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.fragmentManager = fragmentManager;
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
        Book book = list.get(i);
        preferenceManager = new PreferenceManager(context);
        ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.book_listview_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.lv_txt_title);
            viewHolder.author = (TextView) view.findViewById(R.id.lv_txt_author);
            viewHolder.active = (TextView) view.findViewById(R.id.lv_txt_active);
            viewHolder.price = (TextView) view.findViewById(R.id.lv_txt_price);
            viewHolder.img = (ImageView) view.findViewById(R.id.lv_img);
            viewHolder.btnEdit = (Button) view.findViewById(R.id.lv_btn_edit);
            viewHolder.btnDelete = (Button) view.findViewById(R.id.lv_btn_active);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(book.getImageUrl()).into(viewHolder.img);
        viewHolder.title.setText(book.getTitle());
        Author author = AuthorList.getAuthorById(book.getAuthorId());
        viewHolder.author.setText(author.getAuthorName());
        viewHolder.price.setText(String.valueOf(book.getPrice()));

        if(book.getSoLuong() != 0){
            viewHolder.active.setText("Hoạt động");
            viewHolder.active.setTextColor(Color.parseColor("#35a813"));
        }
        else {
            viewHolder.active.setText("Đã ẩn");
            viewHolder.active.setTextColor(Color.parseColor("#d71a00"));
        }

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(book);
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa sach");
                builder.setMessage("Bạn có chắc chắn muốn xóa sach này?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý xóa sach
                        deleteBook(book.getProductId(),book.getImageUrl());
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không làm gì cả nếu người dùng chọn hủy
                    }
                });
                builder.show();
            }
        });


        return view;
    }
    private class ViewHolder{
        TextView title;
        TextView author;
        TextView active;
        TextView price;
        ImageView img;
        Button btnEdit;
        Button btnDelete;
    }
    private void update(Book book){
        Intent intent = new Intent(context, CRUDbookActivity.class);
        intent.putExtra("BOOK_DATA", book);
        context.startActivity(intent);

    }

    private void deleteBook(int bookId,String image)
    {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && bookId != 0 && image != null) {

            DataService dataService = APIService.getService();
            Call<Void> call = dataService.deleteBook(bookId, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        list.removeIf(book -> book.getProductId() == bookId);
                        notifyDataSetChanged();
                        deleteImage(image);
                        // adapter.updateAuthor(response.body());
                        Toast.makeText(inflater.getContext(), "Da xoa thanh cong", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý lỗi khi yêu cầu thất bại
                        try {
                            String errorBody = response.errorBody().string();
                            String code = String.valueOf(response.code());
                            // Xử lý thông tin lỗi ở đây
                            Log.d("Akkkk",errorBody+code);
                            Toast.makeText(inflater.getContext(), "Xoa that bai", Toast.LENGTH_SHORT).show();
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

    private void deleteImage(String image)
    {
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=bookstore2023;AccountKey=XooMlHwngPTe+e6SDqCey/8JcbAaXrhG5aPJuPTBDmOPMxDyD49b+Xa9P+bINImLLq1KrnHptZRk+ASt8P2q2Q==;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        String accountUrl = blobServiceClient.getAccountUrl();
        if (accountUrl != null) {
            Log.d("alllll","Kết nối thành công tới Storage Account");
        } else {
            Log.d("alllll","Kết nối thành công tới Storage Account");
        }
        String containerName = "image";
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        String[] tokens = image.split("/");
        String imageName = tokens[tokens.length - 1];
        String blobName = imageName;
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.delete();
        Log.d("imageDlete","xoa thanh cong");

    }

}
