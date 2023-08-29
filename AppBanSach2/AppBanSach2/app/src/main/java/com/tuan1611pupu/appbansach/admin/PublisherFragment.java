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
import com.tuan1611pupu.appbansach.Adapter.PublisherAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Publisher;
import com.tuan1611pupu.appbansach.model.UserExprech;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublisherFragment extends Fragment implements PublisherDialogFragment.DialogListener {
    ListView listView;
    ArrayList<Publisher> listPublisher;
    PublisherAdapter adapter;
    Button btnAddPublisher;
    private PreferenceManager preferenceManager;
    FragmentManager fragmentManager;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publisher, container, false);
        listView = (ListView) view.findViewById(R.id.admin_lvPublisher);
        btnAddPublisher = view.findViewById(R.id.addPublisher);
        fragmentManager = getParentFragmentManager();

        preferenceManager = new PreferenceManager(getContext());

        listPublisher = new ArrayList<>();
        adapter = new PublisherAdapter(getContext(),listPublisher,fragmentManager);
        if (adapter != null) {
            adapter.setOnEditClickListener(new PublisherAdapter.OnEditClickListener() {
                @Override
                public void onEditClick(Publisher publisher) {
                    showEditPublisherDialog(publisher);
                }
            });
        }
        listView.setAdapter(adapter);
        btnAddPublisher.setOnClickListener(v ->{
            showAddDialog();
        });

        getData();
        return view;
    }



    private void getData(){
        DataService dataService = APIService.getService();
        Call<List<Publisher>> call = dataService.getAllPublisher();
        call.enqueue(new Callback<List<Publisher>>() {
            @Override
            public void onResponse(Call<List<Publisher>> call, Response<List<Publisher>> response) {
                if(listPublisher != null) {
                    listPublisher.clear(); // xóa các phần tử hiện có
                }
                listPublisher.addAll(response.body()); // thêm các phần tử mới
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Publisher>> call, Throwable t) {

            }
        });
    }

    // sua doi thong tin
    private void showEditPublisherDialog(Publisher publisher) {
        PublisherDialogFragment dialog = PublisherDialogFragment.newInstance(publisher);
        dialog.setOnPublisherEditedListener(new PublisherDialogFragment.OnPublisherEditedListener() {
            @Override
            public void onPublisherEdited(Publisher publisher) {
                if(publisher != null){
                    updatePublisher(publisher);

                }
            }
        });
        dialog.show(fragmentManager, "EditAuthorDialogFragment");
    }

    // Hiển thị DialogFragment để nhập thông tin
    private void showAddDialog() {
        PublisherDialogFragment dialog = new PublisherDialogFragment();
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
            Call<Publisher> call = dataService.addPublisher(data, "Bearer " + token);
            call.enqueue(new Callback<Publisher>() {
                @Override
                public void onResponse(Call<Publisher> call, Response<Publisher> response) {
                    // Xử lý phản hồi từ API khi yêu cầu thành công
                    if(response.isSuccessful())
                    {
                        listPublisher.add(response.body());
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Them that bai - co the nha san xuat da them roi", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<Publisher> call, Throwable t) {
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

    private void updatePublisher(Publisher publisher) {
        String token = preferenceManager.getString(Constants.KEY_TOKEN);
        if (token != null && publisher != null) {
            DataService dataService = APIService.getService();
            Call<Publisher> call = dataService.updatePublisher(publisher.getPublisherId(), publisher.getPublisherName(), "Bearer " + token);
            call.enqueue(new Callback<Publisher>() {
                @Override
                public void onResponse(Call<Publisher> call, Response<Publisher> response) {
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
                public void onFailure(Call<Publisher> call, Throwable t) {
                    // Xử lý lỗi khi yêu cầu thất bại
                }
            });
        }
    }
}
