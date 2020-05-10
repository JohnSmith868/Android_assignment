package com.example.librarysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpLoadBooksActivity extends AppCompatActivity {
    TextInputEditText txeTitle;
    TextInputEditText txeAuthor;
    TextInputEditText txeISBN;
    ProgressBar progressBar;
    LoadingDialog loadingDialog;
    MaterialButton button;
    SharedPreferences sharedPreferences;
    private static final String SHARE_PREF = "sharePref";
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_books);
        txeTitle = findViewById(R.id.txe_upload_title);
        txeAuthor = findViewById(R.id.txe_upload_author);
        txeISBN = findViewById(R.id.txe_upload_isbn);
        progressBar = findViewById(R.id.loading_upload_book);
        button = findViewById(R.id.btn_uploadbook);
        sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);

        usertype = sharedPreferences.getString("usertype","normaluser");

        loadingDialog = new LoadingDialog(this);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txeTitle.getText().toString();
                String author = txeAuthor.getText().toString();
                String isbn = txeISBN.getText().toString();

                if(!title.isEmpty()||!author.isEmpty()||!isbn.isEmpty()){
                    makeUpload(title, author,isbn);
                }else {
                    txeAuthor.setError(getString(R.string.alert_empty_title));
                    txeTitle.setError(getString(R.string.alert_empty_author));
                    txeISBN.setError(getString(R.string.alert_empty_isbn));
                }


            }
        });

    }



    public void makeUpload(final String title, final String author, final String isbbn){
        loadingDialog.startloadingDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.backend_url) + "/uploadBook",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("succeed")){
                                Toast.makeText(UpLoadBooksActivity.this,"Upload succeed", Toast.LENGTH_SHORT).show();
                                loadingDialog.dialogDismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UpLoadBooksActivity.this,"error", Toast.LENGTH_SHORT).show();
                            loadingDialog.dialogDismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("bookname",title);
                params.put("author",author);
                params.put("isbn",isbbn);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
