package com.example.librarysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class BookDetailActivity extends AppCompatActivity {

    TextView title;
    TextView author;
    String titledata;
    String authordata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        title = findViewById(R.id.tv_bookdetail_title);
        author = findViewById(R.id.tv_bookdetail_author);

        getData();
        setData();

    }

    private void getData(){
        if(getIntent().hasExtra("title")&&getIntent().hasExtra("author")){
            titledata = getIntent().getStringExtra("title");
            authordata = getIntent().getStringExtra("author");

        }else{
            Toast.makeText(this,"no data",Toast.LENGTH_LONG).show();
        }

    }

    private void setData(){

        title.setText(titledata);
        author.setText(authordata);
    }
}
