package com.example.librarysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button mLoginButton;
    private Button mRegisterButton;
    private Boolean isLogin;
    private TextInputEditText txeUsername;
    private TextInputEditText txePassword;
    private ProgressBar loading;
    private static final String URL_LOGIN = "http://localhost:4000/login/normaluser";
    private static final String SHARE_PREF = "sharePref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginButton = findViewById(R.id.btnLogin);
        mRegisterButton = findViewById(R.id.btnRegister);
        txeUsername = findViewById(R.id.text_input_edit_text_username);
        txePassword = findViewById(R.id.text_input_password);
        loading = findViewById(R.id.loading_main);
        checkIsLogin();
        if(isLogin){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txeUsername.getText().toString();
                String password = txePassword.getText().toString();
                if(!username.isEmpty() || !password.isEmpty()){
                    login(username,password);
                }else{
                    txeUsername.setError("Please enter the username");
                    txePassword.setError("Please enter the password");
                }

            }
        });

    }

    private void login(final String username, final String password) {
        loading.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("l")){
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLogin",true);
                                editor.putInt("userid",jsonObject.getInt("userid"));
                                editor.putString("usertype",jsonObject.getString("usertype"));
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this,"password incorrect",Toast.LENGTH_LONG).show();

                                loading.setVisibility(View.GONE);
                                mLoginButton.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
                        System.out.println(error.toString());
                        loading.setVisibility(View.GONE);
                        mLoginButton.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void checkIsLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF,MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean("isLogin",false);

    }
}
