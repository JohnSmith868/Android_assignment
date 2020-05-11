package com.example.librarysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BookDetailActivity extends AppCompatActivity {

    TextView title;
    TextView author;
    TextView tvBookid;
    TextView tvISBN;
    Button btnMakeappoint;
    String titledata;
    String authordata;
    int bookid;

    String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        title = findViewById(R.id.tv_bookdetail_title);
        author = findViewById(R.id.tv_bookdetail_author);
        tvBookid = findViewById(R.id.tv_bookdetail_bookid);
        tvISBN = findViewById(R.id.tv_bookdetail_isbn);
        btnMakeappoint = findViewById(R.id.btn_bookdetail_makeappoint);

        btnMakeappoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, MakeApointmentActivity.class);
                intent.putExtra("bookid", bookid);
                startActivity(intent);
            }
        });


        getData();
        setData();

    }

    private void getData(){
        if(getIntent().hasExtra("title")&&getIntent().hasExtra("author")&&getIntent().hasExtra("bookid")){
            titledata = getIntent().getStringExtra("title");
            authordata = getIntent().getStringExtra("author");
            bookid = getIntent().getIntExtra("bookid",1);
            isbn = getIntent().getStringExtra("isbn");

        }else{
            Toast.makeText(this,"no data",Toast.LENGTH_LONG).show();
        }

    }

    private void setData(){

        title.setText(titledata);
        author.setText(authordata);
        tvBookid.setText(bookid+"");
        tvISBN.setText(isbn);
    }
}
