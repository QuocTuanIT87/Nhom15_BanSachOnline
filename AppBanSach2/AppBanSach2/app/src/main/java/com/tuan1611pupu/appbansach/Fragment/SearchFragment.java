package com.tuan1611pupu.appbansach.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuan1611pupu.appbansach.Activity.DetailBook;
import com.tuan1611pupu.appbansach.Adapter.BookRecAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    ImageView ivSach;
    ArrayList<Book> data = new ArrayList<>();
    BookRecAdapter adapter_Book;
    RecyclerView rcListBook;
    SearchView svTimSach;

    ImageFilterButton btnBack,btnNext;
    TextView page,pages,ketQua,GoiY;

    int page1 = 1;
    int pages1 = 0;
    String search;

    //RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setControl(view);
        setEvent();

        return view;
    }

    private void setEvent() {

//        khoitao();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        adapter_Book = new BookRecAdapter(getActivity(),data);
        rcListBook.setLayoutManager(layoutManager);
        rcListBook.setAdapter(adapter_Book);

        adapter_Book.setOnItemClickListener(new BookRecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(getContext(), DetailBook.class);
                intent.putExtra("book", book);
                startActivity(intent);
            }
        });

        getBookRandom();
        svTimSach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    GoiY.setVisibility(View.VISIBLE);
                    ketQua.setVisibility(View.GONE);
                    getBookRandom();
                    return false;
                } else {
                    search = newText;
                    GoiY.setVisibility(View.GONE);
                    ketQua.setVisibility(View.VISIBLE);
                    getBookSearchOfTitle(newText,page1);
                    return false;
                }
            }
        });

        btnNext.setOnClickListener(v->{
            if(page1 < pages1){
                page1++;
                Log.d("searec",search);
                getBookSearchOfTitle(search,page1);
            }
        });
        btnBack.setOnClickListener(v->{
            if(page1 > 1){
                page1--;
                getBookSearchOfTitle(search,page1);
            }
        });

    }


    private void khoitao() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        adapter_Book = new BookRecAdapter(getContext(),data);
        rcListBook.setLayoutManager(layoutManager);
        rcListBook.setAdapter(adapter_Book);


    }
    private void getBookRandom() {
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getRandomBook();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(data != null) {
                        data.clear(); // xóa các phần tử hiện có
                    }
                    data.addAll(response.body());
                    for (Book book :data) {
                        if(book.getSoLuong() < 0){
                            data.remove(book);
                        }
                    }
                    if(data.size()>0){
                        pages1 = 1;
                        page1 = 1;
                        pages.setText("1");
                        page.setText("1");
                    }
                    adapter_Book.notifyDataSetChanged();

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });

    }
    private void getBookSearchOfTitle(String search,int page1) {
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getBookOfTitle(search,page1);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(data != null) {
                        data.clear(); // xóa các phần tử hiện có
                    }

                    data.addAll(response.body()); // thêm các phần tử mới
                    for (Book book: data) {
                        if(book.getSoLuong() < 0){
                            data.remove(book);
                        }
                    }
                    if(data.size()>0){
                        pages1 = data.get(0).getPages();
                        pages.setText(pages1+"");
                        page.setText(page1+"");
                    }
                    adapter_Book.notifyDataSetChanged();

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });
    }

    private void setControl(View view) {
        rcListBook = view.findViewById(R.id.rcListBook);
        svTimSach = view.findViewById(R.id.svBook);
        ivSach = view.findViewById(R.id.ivSach);
        page = view.findViewById(R.id.tv_page2);
        pages = view.findViewById(R.id.tv_pages2);
        ketQua = view.findViewById(R.id.tv_ketQuaTimKiem);
        GoiY = view.findViewById(R.id.tv_GoiY);
        btnBack = view.findViewById(R.id.btn_back2);
        btnNext = view.findViewById(R.id.btn_forward2);
    }
}
