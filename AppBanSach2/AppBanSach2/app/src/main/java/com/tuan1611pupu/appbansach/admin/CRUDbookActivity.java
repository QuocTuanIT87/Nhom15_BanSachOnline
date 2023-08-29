package com.tuan1611pupu.appbansach.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.Activity.AdminMenuActivity;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.databinding.ActivityCrudbookBinding;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.BookAdd;
import com.tuan1611pupu.appbansach.model.Category;
import com.tuan1611pupu.appbansach.model.Publisher;
import com.tuan1611pupu.appbansach.model.UserExprech;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CRUDbookActivity extends AppCompatActivity {
    private ActivityCrudbookBinding binding;
    private PreferenceManager preferenceManager;
    private ActivityResultLauncher<String> getImage;
    private Uri imgUri;
    private ArrayList<Category> listCategory;
    private Map<String,String> categoryMap= new HashMap<>();
    private ArrayList<String> categoryName;

    private ArrayList<Author> listAuthor;
    private Map<String,String> authorMap= new HashMap<>();
    private ArrayList<String> authorName;

    private ArrayList<Publisher> listPublisher;
    private Map<String,String> publisherMap= new HashMap<>();
    private ArrayList<String> publisherName;

    private ArrayAdapter categoryAdapter;
    private ArrayAdapter authorAdapter;
    private ArrayAdapter publisherAdapter;
    private AlertDialog dialog;

    private int bookAuthorId;
    private int bookCategoryId;
    private int bookPublisherId;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrudbookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null) {
            book = (Book) intent.getSerializableExtra("BOOK_DATA");
        }


        listAuthor = new ArrayList<>();
        listCategory = new ArrayList<>();
        listPublisher = new ArrayList<>();
        //take image from gallery
        getImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        binding.crudImg.setImageURI(result);
                        imgUri = result;
                    }
                }
        );

        onGetImageClick();

        categoryName = new ArrayList<>();
        categoryAdapter = new ArrayAdapter(getApplicationContext(),R.layout.style_spinner,categoryName);
        binding.crudCategorys.setAdapter(categoryAdapter);
        getCategory();
        binding.crudCategorys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String categoryName = binding.crudCategorys.getItemAtPosition(position).toString();
                String categoryId = categoryMap.get(categoryName);
                // Now you have the authorId corresponding to the selected authorName
                bookCategoryId = Integer.parseInt(categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        authorName = new ArrayList<>();
        authorAdapter = new ArrayAdapter(getApplicationContext(),R.layout.style_spinner,authorName);
        binding.crudAuthor.setAdapter(authorAdapter);
        getAuthor();
        binding.crudAuthor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String authorName2 = binding.crudAuthor.getItemAtPosition(position).toString();
                String authorId = authorMap.get(authorName2);
                // Now you have the authorId corresponding to the selected authorName
                bookAuthorId = Integer.parseInt(authorId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });


        publisherName = new ArrayList<>();
        publisherAdapter = new ArrayAdapter(getApplicationContext(),R.layout.style_spinner,publisherName);
        binding.crudPublisher.setAdapter(publisherAdapter);
        getpublisher();
        binding.crudPublisher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String publisherName = binding.crudPublisher.getItemAtPosition(position).toString();
                String publisherId = publisherMap.get(publisherName);
                // Now you have the authorId corresponding to the selected authorName
                bookPublisherId = Integer.parseInt(publisherId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        if(book != null)
        {
            // sua sach
            getBookData(book);
        }
        binding.crudBtnHuy.setOnClickListener(v->{
            Intent intent2 = new Intent(getApplicationContext(), AdminMenuActivity.class);
            startActivity(intent2);
            finish();
        });
        binding.crudBtnAdd.setOnClickListener(v->{
            if(String.valueOf(binding.crudAuthor.getSelectedItem()) == null)
            {
                showMessage("vui long chon tac gia");
            }else if (binding.crudTitle.getText().toString().isEmpty())
            {
                showMessage("vui long nhap ten sach");
            }else if(binding.crudDes.getText().toString().isEmpty())
            {
                showMessage("vui long them mo ta cho sach");
            }else if(binding.crudYear.getText().toString().isEmpty())
            {
                showMessage("vui long them nam xuat ban");
            }else if(binding.crudPrice.getText().toString().isEmpty())
            {
                showMessage("vui long them gia cho sach");
            }else if(binding.crudInStock.getText().toString().isEmpty())
            {
                showMessage("vui long them so luong");
            }else if(String.valueOf(binding.crudPublisher.getSelectedItem()) == null)
            {
                showMessage("vui long chon nha xuat ban");
            }else if (String.valueOf(binding.crudCategorys.getSelectedItem()) == null)
            {
                showMessage("vui long chon the loai");
            }else if(imgUri == null && book == null)
            {
                showMessage("vui long them anh cho sach");
            }else {
                if(book != null)
                {
                    try {
                        updateBook(book);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        addBook();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        });


    }

    private void addBook() throws IOException {
        binding.crudBtnAdd.setVisibility(View.GONE);
        binding.progressBarLuu.setVisibility(View.VISIBLE);
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=bookstore2023;AccountKey=XooMlHwngPTe+e6SDqCey/8JcbAaXrhG5aPJuPTBDmOPMxDyD49b+Xa9P+bINImLLq1KrnHptZRk+ASt8P2q2Q==;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient= new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        String accountUrl = blobServiceClient.getAccountUrl();
        if (accountUrl != null) {
            Log.d("alllll","Kết nối thành công tới Storage Account");
        } else {
            Log.d("alllll","Kết nối thành công tới Storage Account");
        }
        String containerName = "image";
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        InputStream inputStream = getContentResolver().openInputStream(imgUri);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String blobName1 = binding.crudTitle.getText().toString().trim()+"-"+binding.crudAuthor.getSelectedItem().toString()+hour+minute+second+".jpg";
        String blobName = blobName1.replaceAll("\\s+", "");
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.upload(inputStream, inputStream.available());

        String blobUrl = blobClient.getBlobUrl();




        BookAdd bookAdd = new BookAdd();
        bookAdd.setAuthorId(bookAuthorId);
        bookAdd.setCategoryId(bookCategoryId);
        bookAdd.setPublisherId(bookPublisherId);
        bookAdd.setTitle(binding.crudTitle.getText().toString().trim());
        bookAdd.setDescreption(binding.crudDes.getText().toString().trim());
        bookAdd.setYear(Short.parseShort(binding.crudYear.getText().toString().trim()));
        bookAdd.setPrice(Integer.parseInt(binding.crudPrice.getText().toString().trim()));
        bookAdd.setSoLuong(Integer.parseInt(binding.crudInStock.getText().toString().trim()));
        bookAdd.setImageUrl(blobUrl);

        DataService dataService = APIService.getService();
        Call<Book> call = dataService.addBook(bookAdd,"Bearer "+preferenceManager.getString(Constants.KEY_TOKEN));
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if(response.isSuccessful())
                {
                    binding.crudBtnAdd.setVisibility(View.VISIBLE);
                    binding.progressBarLuu.setVisibility(View.GONE);
                    Toast.makeText(CRUDbookActivity.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
                    startActivity(intent);

                }
                else {

                    // Xử lý thông tin lỗi ở đây
                    binding.crudBtnAdd.setVisibility(View.VISIBLE);
                    binding.progressBarLuu.setVisibility(View.GONE);
                    try {
                        String errorBody = response.errorBody().string();
                        String code = String.valueOf(response.code());
                        // Xử lý thông tin lỗi ở đây
                        Log.d("ADDDD",errorBody + code);
                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Log.d("errr",t+"");
                binding.crudBtnAdd.setVisibility(View.VISIBLE);
                binding.progressBarLuu.setVisibility(View.GONE);
            }
        });

    }

    private  void updateBook(Book book1) throws IOException {
        binding.crudBtnAdd.setVisibility(View.GONE);
        binding.progressBarLuu.setVisibility(View.VISIBLE);

        Book book = new Book();
        if(imgUri != null)
        {
            // xoa anh cu va luu anh moi
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
            // xoa anh cu
            String[] tokens = book1.getImageUrl().split("/");
            String imageName = tokens[tokens.length - 1];
            String blobName2 = imageName;
            BlobClient blobClient2 = containerClient.getBlobClient(blobName2);
            blobClient2.delete();
            // luu anh moi
            InputStream inputStream = getContentResolver().openInputStream(imgUri);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);

            String blobName1 = binding.crudTitle.getText().toString().trim()+"-"+binding.crudAuthor.getSelectedItem().toString()+hour+minute+second+".jpg";
            String blobName = blobName1.replaceAll("\\s+", "");
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.upload(inputStream, inputStream.available());

            String blobUrl = blobClient.getBlobUrl();
            book.setImageUrl(blobUrl);

        }else {
            book.setImageUrl(book1.getImageUrl());
        }
        book.setProductId(book1.getProductId());
        book.setAuthorId(bookAuthorId);
        book.setCategoryId(bookCategoryId);
        book.setPublisherId(bookPublisherId);
        book.setTitle(binding.crudTitle.getText().toString().trim());
        book.setDescreption(binding.crudDes.getText().toString().trim());
        book.setYear(Short.parseShort(binding.crudYear.getText().toString().trim()));
        book.setPrice(Integer.parseInt(binding.crudPrice.getText().toString().trim()));
        book.setSoLuong(Integer.parseInt(binding.crudInStock.getText().toString().trim()));


        DataService dataService = APIService.getService();
        Call<Void> call = dataService.updateBook(book.getProductId(),book,"Bearer "+preferenceManager.getString(Constants.KEY_TOKEN));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    binding.crudBtnAdd.setVisibility(View.VISIBLE);
                    binding.progressBarLuu.setVisibility(View.GONE);
                    Toast.makeText(CRUDbookActivity.this, "Update  thanh cong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
                    startActivity(intent);

                }
                else {

                    // Xử lý thông tin lỗi ở đây
                    binding.crudBtnAdd.setVisibility(View.VISIBLE);
                    binding.progressBarLuu.setVisibility(View.GONE);
                    try {
                        String errorBody = response.errorBody().string();
                        String code = String.valueOf(response.code());
                        // Xử lý thông tin lỗi ở đây
                        Log.d("ADDDD",errorBody + code);
                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("errr",t+"");
                binding.crudBtnAdd.setVisibility(View.VISIBLE);
                binding.progressBarLuu.setVisibility(View.GONE);
            }
        });


    }

    private void getpublisher() {
        Map<String,String> publisherMap2= new HashMap<>();
        DataService dataService = APIService.getService();
        Call<List<Publisher>> call = dataService.getAllPublisher();
        call.enqueue(new Callback<List<Publisher>>() {
            @Override
            public void onResponse(Call<List<Publisher>> call, Response<List<Publisher>> response) {
                if(listPublisher != null) {
                    listPublisher.clear(); // xóa các phần tử hiện có
                }
                listPublisher.addAll(response.body()); // thêm các phần tử mới
                //duyet cac phan tu
                for(Publisher data:listPublisher){
                    String cate = data.getPublisherName();
                    publisherName.add(cate);
                    publisherMap.put(data.getPublisherName(), data.getPublisherId().toString());
                    publisherMap2.put(data.getPublisherId().toString(),data.getPublisherName());
                }
                publisherAdapter.notifyDataSetChanged();
                if(book != null) {
                    String publisherName1 = publisherMap2.get(book.getPublisherId().toString());
                    int selectIndex = publisherAdapter.getPosition(publisherName1);
                    binding.crudPublisher.setSelection(selectIndex);
                }
            }

            @Override
            public void onFailure(Call<List<Publisher>> call, Throwable t) {

            }
        });
    }

    private void getAuthor() {
        Map<String,String> authorMap2= new HashMap<>();
        DataService dataService = APIService.getService();
        Call<List<Author>> call = dataService.getAllAuthor();
        call.enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if(listAuthor != null) {
                    listAuthor.clear(); // xóa các phần tử hiện có
                }
                listAuthor.addAll(response.body()); // thêm các phần tử mới
                //duyet cac phan tu
                for(Author data:listAuthor){
                    String cate = data.getAuthorName();
                    authorName.add(cate);
                    authorMap.put(data.getAuthorName(), data.getAuthorId().toString());
                    authorMap2.put(data.getAuthorId().toString(),data.getAuthorName());
                }
                authorAdapter.notifyDataSetChanged();

                if(book != null) {
                    String authorName1 = authorMap2.get(book.getAuthorId().toString());
                    int selectIndex = authorAdapter.getPosition(authorName1);
                    binding.crudAuthor.setSelection(selectIndex);
                    Log.d("abbbbb",authorName1+ authorMap+" "+book.getAuthorId().toString()+authorMap);
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {

            }
        });
    }

    private void getCategory() {
        Map<String,String> categoryMap2= new HashMap<>();
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
                for(Category data:listCategory){
                    String cate = data.getCategoryName();
                    categoryName.add(cate);
                    categoryMap.put(data.getCategoryName(), data.getCategoryId().toString());
                    categoryMap2.put(data.getCategoryId().toString(),data.getCategoryName());
                }
                categoryAdapter.notifyDataSetChanged();
                if(book != null) {
                    String category1 = categoryMap2.get(book.getCategoryId().toString());
                    int selectIndex = categoryAdapter.getPosition(category1);
                    binding.crudCategorys.setSelection(selectIndex);
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });


    }

    private void showMessage (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void onGetImageClick(){
        binding.crudBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage.launch("image/*");
            }
        });
    }

    public void getBookData(Book book)
    {
        binding.crudTitle.setText(book.getTitle());
        binding.crudInStock.setText(book.getSoLuong().toString());
        binding.crudPrice.setText(book.getPrice().toString());
        binding.crudDes.setText(book.getDescreption());
        binding.crudYear.setText(book.getYear().toString());
        Glide.with(getApplicationContext()).load(book.getImageUrl()).into(binding.crudImg);
    }



}