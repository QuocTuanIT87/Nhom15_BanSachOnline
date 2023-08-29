package com.tuan1611pupu.appbansach.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tuan1611pupu.appbansach.Adapter.BookListViewAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminListbook extends Fragment {
    //Realtime Database

    //biding
    ListView listView;
    ArrayList<Book> listBook;
    BookListViewAdapter adapter;
    EditText bookOfTitle;
    Button btnTimKiem;
    Button btnAddBook;
    androidx.constraintlayout.utils.widget.ImageFilterButton btnBack,btnNext;
    TextView bookZero,page,pages;
    LinearLayout linearLayout;
    private PreferenceManager preferenceManager;
    FragmentManager fragmentManager;
    int tpages;
    int pageIndext = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_listbook, container, false);
        listView = (ListView) view.findViewById(R.id.admin_lv);
        bookOfTitle = (EditText) view.findViewById(R.id.tvBookOfTitle);
        linearLayout = view.findViewById(R.id.linear_book);
        page = view.findViewById(R.id.tv_page);
        pages = view.findViewById(R.id.tv_pages);
        btnBack = view.findViewById(R.id.btn_back);
        btnNext = view.findViewById(R.id.btn_forward);
        btnTimKiem = view.findViewById(R.id.btnTimkiem);
        btnAddBook = view.findViewById(R.id.btnAddBook);
        bookZero = view.findViewById(R.id.tv_bookZero);
        fragmentManager = getParentFragmentManager();
        preferenceManager = new PreferenceManager(getContext());

        btnAddBook.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), CRUDbookActivity.class);
            startActivity(intent);

        });
        listBook = new ArrayList<>();
        adapter = new BookListViewAdapter(getContext(),listBook,fragmentManager);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //nothing to do

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if(lastInScreen == totalItemCount) {
                    linearLayout.setVisibility(View.VISIBLE);
                }else{
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        btnTimKiem.setOnClickListener(v ->{
            if(!bookOfTitle.getText().toString().trim().isEmpty())
            {
                getDataOfTitle(bookOfTitle.getText().toString().trim(),pageIndext);
            }else {
                getData(pageIndext);
            }
        });
        getData(pageIndext);
        btnNext.setOnClickListener(v->{
            if(tpages > pageIndext){
                pageIndext ++;
                getData(pageIndext);
            }
        });
        btnBack.setOnClickListener(v->{
            if(pageIndext > 1){
                pageIndext --;
                getData(pageIndext);
            }
        });
        return view;
    }

    private void getDataOfTitle(String search,int page1) {
        // de sua lai da
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getBookOfTitle(search,page1);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(listBook != null) {
                        listBook.clear(); // xóa các phần tử hiện có
                    }
                    listBook.addAll(response.body()); // thêm các phần tử mới
                    adapter.notifyDataSetChanged();
                    if(listBook.size() == 0)
                    {
                        bookZero.setVisibility(View.VISIBLE);
                    }else {
                        tpages = listBook.get(0).getPages();
                        pages.setText(tpages+"");
                        page.setText(page1+"");
                    }

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });
    }

    private void getData(int page1){
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getAllBooks(page1);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(listBook != null) {
                        listBook.clear(); // xóa các phần tử hiện có
                    }
                    listBook.addAll(response.body()); // thêm các phần tử mới
                    adapter.notifyDataSetChanged();
                    if(listBook.size() == 0)
                    {
                        bookZero.setVisibility(View.VISIBLE);
                    }else {
                        tpages = listBook.get(0).getPages();
                        pages.setText(tpages+"");
                        page.setText(page1+"");
                    }

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });

    }
}