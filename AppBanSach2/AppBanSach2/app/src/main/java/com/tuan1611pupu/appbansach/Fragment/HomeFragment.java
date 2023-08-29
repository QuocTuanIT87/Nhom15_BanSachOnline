package com.tuan1611pupu.appbansach.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tuan1611pupu.appbansach.Activity.DetailBook;
import com.tuan1611pupu.appbansach.Adapter.BookRecAdapter;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    ArrayList<Book> data_book = new ArrayList<>();
    ArrayList<Book> listBook = new ArrayList<>();
    BookRecAdapter adapter_BookRec;
    BookRecAdapter adapter_BookRec2;
    RecyclerView recyclerViewBook;
    RecyclerView rcViewBook;
    ViewFlipper viewFlipper;
    ImageFilterButton btnBack,btnNext;
    TextView page,pages;
    Spinner spLoc;
    private ArrayAdapter categoryAdapter;
    private Map<String,String> categoryMap= new HashMap<>();
    private ArrayList<String> categoryName = new ArrayList<>();
    private ArrayList<Category> listCategory = new ArrayList<>();
    int page1 = 1;
    int pages1 = 0;
    int categoryId = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setControl(view);
        setEvent();

        return view;
    }

    private void Banner(){
        ImageView v1 = new ImageView(requireContext().getApplicationContext());
        ImageView v2 = new ImageView(requireContext().getApplicationContext());
        ImageView v3 = new ImageView(requireContext().getApplicationContext());
        v1.setImageResource(R.drawable.banner1);
        v2.setImageResource(R.drawable.banner2);
        v1.setScaleType(ImageView.ScaleType.FIT_XY);
        v2.setScaleType(ImageView.ScaleType.FIT_XY);
        viewFlipper.addView(v1);
        viewFlipper.addView(v2);
        viewFlipper.setAutoStart(true);
    }
    private void setEvent() {
        Banner();
        getBookRandom();
        adapter_BookRec2 = new BookRecAdapter(getActivity(),listBook);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter_BookRec2.setOnItemClickListener(new BookRecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                // Handle the item click with book data
                Intent intent = new Intent(getContext(), DetailBook.class);
                intent.putExtra("book", book);
                startActivity(intent);
            }
        });
        recyclerViewBook.setAdapter(adapter_BookRec2);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        adapter_BookRec = new BookRecAdapter(getActivity(),data_book);
        rcViewBook.setLayoutManager(layoutManager);
        adapter_BookRec.setOnItemClickListener(new BookRecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                // Handle the item click with book data
                Intent intent = new Intent(getContext(), DetailBook.class);
                intent.putExtra("book", book);
                startActivity(intent);
            }
        });
        rcViewBook.setAdapter(adapter_BookRec);

        categoryName = new ArrayList<>();
        categoryAdapter = new ArrayAdapter(getContext(),R.layout.style_spinner,categoryName);
        spLoc.setAdapter(categoryAdapter);
        getAllCategory();
        spLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String categoryName = spLoc.getItemAtPosition(position).toString();
                String categoryId1 = categoryMap.get(categoryName);
                // Now you have the authorId corresponding to the selected authorName
                categoryId = Integer.parseInt(categoryId1);
                page1 = 1;
                pages1 = 0;
                if(categoryId != 0){
                    getBookOfCategory(categoryId,page1);
                }else {
                    getAllBook(page1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        btnNext.setOnClickListener(v->{
            if(page1 < pages1){
                page1++;
                if(categoryId == 0){
                    getAllBook(page1);
                }else {
                    getBookOfCategory(categoryId,page1);
                }
            }

        });
        btnBack.setOnClickListener(v->{
            if(page1 > 1){
                page1--;
                if(categoryId == 0){
                    getAllBook(page1);
                }else {
                    getBookOfCategory(categoryId,page1);
                }
            }
        });


    }

    private void getBookOfCategory(int categoryId,int page1) {
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getBookOfCategory(categoryId,page1);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(data_book != null) {
                        data_book.clear();
                    }
                    data_book.addAll(response.body());
                    for (Book book: data_book) {
                        if(book.getSoLuong() < 0){
                            data_book.remove(book);
                        }
                    }

                    adapter_BookRec.notifyDataSetChanged();
                    if(data_book.size()>0){
                        pages1 = data_book.get(0).getPages();
                        pages.setText(pages1+"");
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

    private void getAllCategory(){
        DataService dataService = APIService.getService();
        Call<List<Category>> call = dataService.getAllCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(listCategory != null) {
                    listCategory.clear(); // xóa các phần tử hiện có
                }
                listCategory.addAll(response.body()); // thêm các phần tử mới
                //duyet cac phan tu
                categoryMap.put("All","0");
                categoryName.add("All");
                for(Category data:listCategory){
                    String cate = data.getCategoryName();
                    categoryName.add(cate);
                    categoryMap.put(data.getCategoryName(), data.getCategoryId().toString());
                }
                categoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private void getBookRandom() {
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getRandomBook();
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(listBook != null) {
                        listBook.clear(); // xóa các phần tử hiện có
                    }
                    listBook.addAll(response.body());
                    for (Book book :listBook) {
                        if(book.getSoLuong() < 0){
                            listBook.remove(book);
                        }
                    }
                    adapter_BookRec2.notifyDataSetChanged();

                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });
    }

    private void getAllBook(int page1) {
        DataService dataService = APIService.getService();
        Call<List<Book>> call = dataService.getAllBooks(page1);
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if(response.isSuccessful())
                {
                    if(data_book != null) {
                        data_book.clear(); // xóa các phần tử hiện có
                    }
                    data_book.addAll(response.body()); // thêm các phần tử mới
                    for (Book book: data_book) {
                        if(book.getSoLuong() < 0){
                            data_book.remove(book);
                        }
                    }
                    adapter_BookRec.notifyDataSetChanged();
                    if(data_book.size()>0){
                        pages1 = data_book.get(0).getPages();
                        pages.setText(pages1+"");
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

    private void setControl(View view) {
        rcViewBook = view.findViewById(R.id.rcListBook);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        recyclerViewBook = view.findViewById(R.id.recyclerViewBook);
        btnBack = view.findViewById(R.id.btn_back1);
        btnNext = view.findViewById(R.id.btn_forward1);
        page = view.findViewById(R.id.tv_page1);
        pages = view.findViewById(R.id.tv_pages1);
        spLoc = view.findViewById(R.id.locTheoTheLoai);


    }
}
