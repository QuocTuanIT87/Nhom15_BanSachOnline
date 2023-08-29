package com.tuan1611pupu.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater = null;
    private PreferenceManager preferenceManager;
    ArrayList<Author> list;
    {
        list = new ArrayList<>();
    }
    FragmentManager fragmentManager;

    public AuthorAdapter(Context context, ArrayList<Author> list, FragmentManager fragmentManager) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    public interface OnEditClickListener {
        void onEditClick(Author author);
    }

    private OnEditClickListener onEditClickListener;

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
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
        Author author = list.get(i);
        preferenceManager = new PreferenceManager(context);
        AuthorAdapter.ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.author_listview_layout, viewGroup, false);
            viewHolder = new AuthorAdapter.ViewHolder();
            viewHolder.authorId = (TextView) view.findViewById(R.id.tv_idAuthor);
            viewHolder.authorNmae = (TextView) view.findViewById(R.id.tv_authorName);
            viewHolder.btnEdit = (Button) view.findViewById(R.id.lv_btn_edit_author);
            viewHolder.btnDelete = (Button) view.findViewById(R.id.lv_btn_delete_author);
            view.setTag(viewHolder);
        }else{
            viewHolder = (AuthorAdapter.ViewHolder) view.getTag();
        }
        viewHolder.authorId.setText(author.getAuthorId().toString());
        viewHolder.authorNmae.setText(String.valueOf(author.getAuthorName()));


        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditClickListener != null) {
                    onEditClickListener.onEditClick(author);
                }
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa tác giả");
                builder.setMessage("Bạn có chắc chắn muốn xóa tác giả này?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý xóa tác giả
                        deleteAuthor(author.getAuthorId());
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
        TextView authorId;
        TextView authorNmae;
        Button btnEdit;
        Button btnDelete;
    }

    private void deleteAuthor(int authorId)
    {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && authorId != 0) {

            DataService dataService = APIService.getService();
            Call<Void> call = dataService.deleteAuthor(authorId, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        list.removeIf(author -> author.getAuthorId() == authorId);
                        notifyDataSetChanged();
                        // adapter.updateAuthor(response.body());
                        Toast.makeText(inflater.getContext(), "Da xoa thanh cong", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý lỗi khi yêu cầu thất bại
                        try {
                            String errorBody = response.errorBody().string();
                            String code = String.valueOf(response.code());
                            // Xử lý thông tin lỗi ở đây
                            Toast.makeText(inflater.getContext(), "Xoa that bai", Toast.LENGTH_SHORT).show();
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

}
