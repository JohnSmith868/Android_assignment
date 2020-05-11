package com.example.librarysystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapterOfMyBooks extends RecyclerView.Adapter<BookAdapterOfMyBooks.MyViewHolder> {

    Context mContext;
    List<BookCardOfMyBooks> bookList;

    public BookAdapterOfMyBooks(Context mContext, List<BookCardOfMyBooks> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.book_appointed_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_title.setText(bookList.get(position).getTitle());
        holder.tv_author.setText(bookList.get(position).getAuthor());
        holder.tv_collectDate.setText(mContext.getString(R.string.alert_please_collect)+bookList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        TextView tv_author;
        TextView tv_collectDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.book_appointed_card_author);
            tv_title = itemView.findViewById(R.id.book_appointed_card_title);
            tv_collectDate=itemView.findViewById(R.id.book_appointed_card_collectdate);

        }
    }
}
