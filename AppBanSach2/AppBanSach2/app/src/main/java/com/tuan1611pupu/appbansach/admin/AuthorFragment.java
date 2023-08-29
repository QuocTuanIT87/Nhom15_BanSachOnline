package com.tuan1611pupu.appbansach.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tuan1611pupu.appbansach.Adapter.AuthorAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.AuthorList;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorFragment extends Fragment implements AuthorDialogFragment.DialogListener {
    ListView listView;
    ArrayList<Author> listAuthor;
    AuthorAdapter adapter;
    Button btnAddAuthor;
    FragmentManager fragmentManager;
    private PreferenceManager preferenceManager;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_author, container, false);
        listView = (ListView) view.findViewById(R.id.admin_lvAuthor);
        btnAddAuthor = view.findViewById(R.id.addAuthor);
        fragmentManager = getParentFragmentManager();
        preferenceManager = new PreferenceManager(getContext());
        listAuthor = new ArrayList<>();
        adapter = new AuthorAdapter(getContext(),listAuthor,fragmentManager);
        if (adapter != null) {
            adapter.setOnEditClickListener(new AuthorAdapter.OnEditClickListener() {
                @Override
                public void onEditClick(Author author) {
                    showEditAuthorDialog(author);
                }
            });
        }
        listView.setAdapter(adapter);
        btnAddAuthor.setOnClickListener(v ->{
            showAddDialog();
        });

        getData();
        return view;
    }



    private void getData(){
        DataService dataService = APIService.getService();
        Call<List<Author>> call = dataService.getAllAuthor();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if(listAuthor != null) {
                    listAuthor.clear(); // xóa các phần tử hiện có
                }
                listAuthor.addAll(response.body()); // thêm các phần tử mới
                for (Author author: listAuthor) {
                    AuthorList.addAuthor(author);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {

            }
        });
    }



    // sua doi thong tin
    private void showEditAuthorDialog(Author author) {
        AuthorDialogFragment dialog = AuthorDialogFragment.newInstance(author);
        dialog.setOnAuthorEditedListener(new AuthorDialogFragment.OnAuthorEditedListener() {
            @Override
            public void onAuthorEdited(Author author) {
                // Cập nhật thông tin tác giả trong danh sách và adapter
                //adapter.updateAuthor(author);
                if(author != null){
                    updateAuthor(author);

                }
            }
        });
        dialog.show(fragmentManager, "EditAuthorDialogFragment");
    }



    // Hiển thị DialogFragment để nhập thông tin
    private void showAddDialog() {
        AuthorDialogFragment dialog = new AuthorDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "AddDialogFragment");
    }
    // Nhận dữ liệu trả về từ DialogFragment
    @Override
    public void onDialogPositiveClick(String data) {
        // Thêm dữ liệu vào danh sách và cập nhật giao diện
        //listAuthor.add(data);
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if(token != null && data != null)
        {
            DataService dataService = APIService.getService();
            Call<Author> call = dataService.addAuthor(data, "Bearer " + token);
            call.enqueue(new Callback<Author>() {
                @Override
                public void onResponse(Call<Author> call, Response<Author> response) {
                    // Xử lý phản hồi từ API khi yêu cầu thành công
                    if(response.isSuccessful())
                    {
                        listAuthor.add(response.body());
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Them that bai - co the tac gia da them roi", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<Author> call, Throwable t) {
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

    private void updateAuthor(Author author) {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && author != null) {
            DataService dataService = APIService.getService();
            Call<Author> call = dataService.updateAuthor(author.getAuthorId(), author.getAuthorName(), "Bearer " + token);
            call.enqueue(new Callback<Author>() {
                @Override
                public void onResponse(Call<Author> call, Response<Author> response) {
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
                public void onFailure(Call<Author> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại
                }
            });
        }
    }


}
