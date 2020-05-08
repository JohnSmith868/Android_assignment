package com.example.librarysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchBookFragment extends Fragment {

    private RecyclerView searchBookRecyclerView;
    private List<BookCard> bookLists;
    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookLists = new ArrayList<>();
        bookLists.add(new BookCard("book1","author1"));
        bookLists.add(new BookCard("book2","author2"));
        bookLists.add(new BookCard("book3","author3"));
        bookLists.add(new BookCard("book4","author4"));
        bookLists.add(new BookCard("book5","author5"));
        bookLists.add(new BookCard("book6","author6"));
        bookLists.add(new BookCard("book7","author7"));
        bookLists.add(new BookCard("book8","author8"));
        bookLists.add(new BookCard("book9","author9"));
        bookLists.add(new BookCard("book10","author11"));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        v = inflater.inflate(R.layout.fragment_searchbook,container,false);
        searchBookRecyclerView = (RecyclerView) v.findViewById(R.id.search_book_rv);
        BookAdapter bookAdapter = new BookAdapter(getContext(),bookLists);
        searchBookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBookRecyclerView.setAdapter(bookAdapter);
        return v;
    }
}
