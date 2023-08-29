package com.tuan1611pupu.appbansach.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.Api.APIService;
import com.tuan1611pupu.appbansach.Api.DataService;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;
import com.tuan1611pupu.appbansach.model.BookInfo;
import com.tuan1611pupu.appbansach.utilities.Constants;
import com.tuan1611pupu.appbansach.utilities.PreferenceManager;

import java.util.List;

import io.jsonwebtoken.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBook extends AppCompatActivity {

    ImageView ivSachDetail;
    Button btnGiamSoLuong, btnTangSoLuong, btnThemGioHang;
    TextView tvTitleDetail,tvGiaDetail, tvSoLuong, tvMoTa, tvTacGia,tvNamXB,tvTheLoai;
    int numOfBook = 1;
    private PreferenceManager preferenceManager;
//    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setControl();
        setEvent();
    }
    private void setEvent() {
        btnTangSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfBook += 1;
                tvSoLuong.setText(String.valueOf(numOfBook));
            }
        });
        btnGiamSoLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numOfBook > 1) {
                    numOfBook -= 1;
                    tvSoLuong.setText(String.valueOf(numOfBook));
                }

            }
        });
        Book b = (Book) getIntent().getSerializableExtra("book");
        getBookInfo(b);
        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = preferenceManager.getString(Constants.KEY_TOKEN);
                String memberId = preferenceManager.getString(Constants.KEY_MEMBER_ID);
                if (token != null) {
                    DataService dataService = APIService.getService();
                    Call<Void> call = dataService.addCartItem(numOfBook,b.getProductId(),memberId, "Bearer " + token);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // adapter.updateAuthor(response.body());
                                Toast.makeText( getApplicationContext(),"Da thanh cong", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý lỗi khi yêu cầu thất bại
                                try {
                                    String errorBody = response.errorBody().string();
                                    String code = String.valueOf(response.code());
                                    // Xử lý thông tin lỗi ở đây
                                    Toast.makeText(getApplicationContext(), " that bai", Toast.LENGTH_SHORT).show();
                                    Log.d("Akkkk",errorBody+code);
                                } catch (IOException e) {
                                    e.printStackTrace();

                                } catch (java.io.IOException e) {
                                    throw new RuntimeException(e);
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
        });
    }


    private void getBookInfo(Book b) {
        DataService dataService = APIService.getService();
        Call<BookInfo> call = dataService.getBookInfo(b.getProductId());
        call.enqueue(new Callback<BookInfo>() {
            @Override
            public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                if(response.isSuccessful())
                {
                    BookInfo bookInfo = response.body();
                    tvTacGia.setText(bookInfo.getAuthorName());
                    tvTheLoai.setText(bookInfo.getCategoryName());
                    tvNamXB.setText(bookInfo.getYear().toString());
                    tvMoTa.setText(bookInfo.getDescreption());
                    tvSoLuong.setText(String.valueOf(numOfBook));
                    tvTitleDetail.setText(bookInfo.getTitle());
                    tvGiaDetail.setText(String.valueOf(bookInfo.getPrice()+" VND"));
                    Glide.with(getApplicationContext()).load(bookInfo.getImageUrl()).into(ivSachDetail);


                }else {
                    // loi
                }
            }

            @Override
            public void onFailure(Call<BookInfo> call, Throwable t) {

            }
        });
    }

    private void setControl() {
        ivSachDetail = findViewById(R.id.ivSachDetail);
        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvGiaDetail = findViewById(R.id.tvGiaDetail);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvTacGia = findViewById(R.id.tvTacGia);
        tvNamXB = findViewById(R.id.tvNamXB);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        btnGiamSoLuong = findViewById(R.id.btnGiamSoLuong);
        btnTangSoLuong = findViewById(R.id.btnTangSoLuong);
        btnThemGioHang = findViewById(R.id.btnThemGioHang);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}