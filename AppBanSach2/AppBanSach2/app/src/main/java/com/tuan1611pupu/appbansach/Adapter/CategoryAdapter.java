package com.tuan1611pupu.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Category;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater = null;
    private PreferenceManager preferenceManager;
    ArrayList<Category> list;
    {
        list = new ArrayList<>();
    }
    FragmentManager fragmentManager;
    public CategoryAdapter(Context context, ArrayList<Category> list, FragmentManager fragmentManager) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.fragmentManager = fragmentManager;
    }
    public interface OnEditClickListener {
        void onEditClick(Category category);
    }

    private CategoryAdapter.OnEditClickListener onEditClickListener;

    public void setOnEditClickListener(CategoryAdapter.OnEditClickListener listener) {
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
        Category category = list.get(i);
        preferenceManager = new PreferenceManager(context);
        CategoryAdapter.ViewHolder viewHolder;
        if(view == null){
            view = inflater.inflate(R.layout.category_listview_layout, viewGroup, false);
            viewHolder = new CategoryAdapter.ViewHolder();
            viewHolder.categoryId = (TextView) view.findViewById(R.id.tv_idCategory);
            viewHolder.categoryName = (TextView) view.findViewById(R.id.tv_categoryName);
            viewHolder.btnEdit = (Button) view.findViewById(R.id.lv_btn_edit_category);
            viewHolder.btnDelete = (Button) view.findViewById(R.id.lv_btn_delete_category);
            view.setTag(viewHolder);
        }else{
            viewHolder = (CategoryAdapter.ViewHolder) view.getTag();
        }
        viewHolder.categoryId.setText(category.getCategoryId().toString());
        viewHolder.categoryName.setText(String.valueOf(category.getCategoryName()));

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditClickListener != null) {
                    onEditClickListener.onEditClick(category);
                }
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa the laoi");
                builder.setMessage("Bạn có chắc chắn muốn xóa the loai này?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý xóa the loai
                        deleteCategory(category.getCategoryId());
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
        TextView categoryId;
        TextView categoryName;
        Button btnEdit;
        Button btnDelete;
    }
    private void deleteCategory(int categoryId)
    {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && categoryId != 0) {

            DataService dataService = APIService.getService();
            Call<Void> call = dataService.deleteCategory(categoryId, "Bearer " + token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        list.removeIf(category -> category.getCategoryId() == categoryId);
                        notifyDataSetChanged();
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

}
