package com.tuan1611pupu.appbansach.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tuan1611pupu.appbansach.Adapter.AuthorAdapter;
import com.tuan1611pupu.appbansach.Adapter.CategoryAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Category;
import com.tuan1611pupu.appbansach.model.UserExprech;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment implements CategoryDialogFragment.DialogListener{


    ListView listView;
    ArrayList<Category> listCategory;
    CategoryAdapter adapter;
    Button btnAddCategory;
    private PreferenceManager preferenceManager;
    FragmentManager fragmentManager;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) view.findViewById(R.id.admin_lvCategory);
        btnAddCategory = view.findViewById(R.id.addCategory);
        fragmentManager = getParentFragmentManager();
        preferenceManager = new PreferenceManager(getContext());

        listCategory = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(),listCategory,fragmentManager);
        if (adapter != null) {
            adapter.setOnEditClickListener(new CategoryAdapter.OnEditClickListener() {
                @Override
                public void onEditClick(Category category) {
                    showEditAuthorDialog(category);
                }
            });
        }
        listView.setAdapter(adapter);
        btnAddCategory.setOnClickListener(v ->{
            showAddDialog();
        });

        getData();
        return view;
    }



    private void getData(){
        DataService dataService = APIService.getService();
        Call<List<Category>> call = dataService.getAllCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(listCategory != null) {
                    listCategory.clear(); // xóa các phần tử hiện có
                }
                listCategory.addAll(response.body()); // thêm các phần tử mới
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    // sua doi thong tin
    private void showEditAuthorDialog(Category category) {
        CategoryDialogFragment dialog = CategoryDialogFragment.newInstance(category);
        dialog.setOnCategoryEditedListener(new CategoryDialogFragment.OnCategoryEditedListener() {
            @Override
            public void onCategoryEdited(Category category) {
                // Cập nhật thông tin tác giả trong danh sách và adapter
                //adapter.updateAuthor(author);
                if(category != null){
                   updateCategory(category);

                }
            }
        });
        dialog.show(fragmentManager, "EditCategoryDialogFragment");
    }

    // Hiển thị DialogFragment để nhập thông tin
    private void showAddDialog() {
        CategoryDialogFragment dialog = new CategoryDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddDialogFragment");
    }
    // Nhận dữ liệu trả về từ DialogFragment
    @Override
    public void onDialogPositiveClick(String data) {
        // Thêm dữ liệu vào danh sách và cập nhật giao diện
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if(token != null && data != null)
        {
            DataService dataService = APIService.getService();
            Call<Category> call = dataService.addCategory(data, "Bearer " + token);
            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    // Xử lý phản hồi từ API khi yêu cầu thành công
                    if(response.isSuccessful())
                    {
                        listCategory.add(response.body());
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Them that bai - co the the loai da them roi", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại

                }
            });

        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick() {
        // Không làm gì cả
    }
    private void updateCategory(Category category) {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && category != null) {
            DataService dataService = APIService.getService();
            Call<Category> call = dataService.updateCategory(category.getCategoryId(), category.getCategoryName(), "Bearer " + token);
            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    if (response.isSuccessful()) {

                        // adapter.updateAuthor(response.body());
                        Toast.makeText(getContext(), "Oki", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý lỗi khi yêu cầu thất bại
                        try {
                            String errorBody = response.errorBody().string();
                            String code = String.valueOf(response.code());
                            // Xử lý thông tin lỗi ở đây
                            Log.d("Akkkk",errorBody+code);
                        } catch (IOException e) {

                            e.printStackTrace();

                        }
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại
                }
            });
        }
    }
}