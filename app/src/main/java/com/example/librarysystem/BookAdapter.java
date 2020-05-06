package com.example.librarysystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    Context mContext;
    List<BookCard> bookList;

    public BookAdapter(Context mContext, List<BookCard> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_title.setText(bookList.get(position).getTitle());
        holder.tv_author.setText(bookList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        TextView tv_author;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.book_card_author);
            tv_title = itemView.findViewById(R.id.book_card_title);
        }
    }
}
