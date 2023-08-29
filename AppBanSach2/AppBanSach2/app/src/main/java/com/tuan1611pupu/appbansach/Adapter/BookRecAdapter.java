package com.tuan1611pupu.appbansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Book;

import java.util.List;

public class BookRecAdapter extends RecyclerView.Adapter<BookRecAdapter.BookViewHolder> {

    private List<Book> bookList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public BookRecAdapter(Context context,List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);

        return new BookViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bookTitleTextView.setText(book.getTitle());
        holder.bookPriceTextView.setText(book.getPrice()+" VND");
        Glide.with(context).load(book.getImageUrl()).into(holder.bookImageView);
        holder.itemView.setOnClickListener(v->{
            if (listener != null) {
                listener.onItemClick(book);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImageView;
        TextView bookTitleTextView;
        TextView bookPriceTextView;

        BookViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.ivSach);
            bookTitleTextView = itemView.findViewById(R.id.tvTenSach);
            bookPriceTextView = itemView.findViewById(R.id.tvGia);
        }
    }
}

