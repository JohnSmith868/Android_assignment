package com.example.librarysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MakeApointmentActivity extends AppCompatActivity {

    ProgressBar loading;
    TextView tv_message;
    SharedPreferences sharedPreferences;
    private static final String SHARE_PREF = "sharePref";
    int userid;
    int bookid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_apointment);
        loading = findViewById(R.id.loading_make_appointment);
        tv_message = findViewById(R.id.tv_make_appointment_notice);
        sharedPreferences = getSharedPreferences(SHARE_PREF,MODE_PRIVATE);
        userid = sharedPreferences.getInt("userid",-1);

        loading.setVisibility(View.VISIBLE);

        makeAppointment();
    }

    private void makeAppointment() {

        if(userid!=-1&&getIntent().hasExtra("bookid")){
            bookid = getIntent().getIntExtra("bookid",1);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.backend_url) + "/makeApoint",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.has("apointid")){
                                    loading.setVisibility(View.GONE);
                                    tv_message.setText("succeed to make appointment");
                                    tv_message.setVisibility(View.VISIBLE);

                                }

                            } catch (JSONException e) {
                                loading.setVisibility(View.GONE);
                                tv_message.setText("some thing went wrong, fail to make appointment");
                                tv_message.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            loading.setVisibility(View.GONE);
                            tv_message.setText("some thing went wrong, fail to make appointment");
                            tv_message.setVisibility(View.VISIBLE);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("normaluserid",userid+"");
                    params.put("bookid",bookid+"");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }

    }
}
