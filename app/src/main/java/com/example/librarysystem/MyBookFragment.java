package com.example.librarysystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBookFragment extends Fragment {

    private RecyclerView myBookRecyclerView;
    ProgressBar loading;
    BookAdapterOfMyBooks myBookAdapter;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    private static final String SHARE_PREF = "sharePref";
    int userid;
    private List<BookCardOfMyBooks> myBookLists;


    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBookLists = new ArrayList<>();
        System.out.println("sequence 1");
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PREF, Context.MODE_PRIVATE);
        userid = sharedPreferences.getInt("userid",-1);






    }

    private void getBooks() {
        System.out.println("sequence 2");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.backend_url)+ "/getMyAppointment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>0){
                                System.out.println("jsonarraylength:"+jsonArray.length());
                                for(int i = 0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    myBookLists.add(new BookCardOfMyBooks(jsonObject.getString("bookname"),jsonObject.getString("author"), jsonObject.getInt("apointid"),jsonObject.getString("deadline")));
                                    System.out.println("succeed add myappointmentlist");
                                }

                                myBookRecyclerView.setAdapter(myBookAdapter);
                                loading.setVisibility(View.GONE);
                                System.out.println("sequence 4");
                            }else{
                                System.out.println("json array < 0");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("onErrop"+error.toString());

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userid",userid+"");

                return params;
            }
        };


        requestQueue.add(stringRequest);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        v = inflater.inflate(R.layout.fragment_mybook,container,false);

        myBookRecyclerView = (RecyclerView) v.findViewById(R.id.my_book_rv);
        loading = (ProgressBar) v.findViewById(R.id.loading_my_book_fragment);
        loading.setVisibility(View.VISIBLE);




        myBookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myBookAdapter = new BookAdapterOfMyBooks(getContext(),myBookLists);
        requestQueue = Volley.newRequestQueue(getActivity());
        getBooks();

        System.out.println("sequence 3");
        return v;
    }
}
