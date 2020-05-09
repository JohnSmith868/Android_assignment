package com.example.librarysystem;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import java.util.Map;

public class SearchBookFragment extends Fragment {

    private RecyclerView searchBookRecyclerView;
    ProgressBar loading;
    BookAdapter bookAdapter;
    RequestQueue requestQueue;
    private List<BookCard> bookLists;


    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookLists = new ArrayList<>();
        System.out.println("sequence 1");






    }

    private void getBooks() {
        System.out.println("sequence 2");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://localhost:4000" + "/getAllBooks",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>0){
                                System.out.println("jsonarraylength:"+jsonArray.length());
                                for(int i = 0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    bookLists.add(new BookCard(jsonObject.getString("bookname"),jsonObject.getString("author")));
                                    System.out.println("succeed add booklist");
                                }

                                searchBookRecyclerView.setAdapter(bookAdapter);
                                loading.setVisibility(View.GONE);
                                System.out.println("sequence 4");
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
                return super.getParams();
            }
        };


        requestQueue.add(stringRequest);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        v = inflater.inflate(R.layout.fragment_searchbook,container,false);

        searchBookRecyclerView = (RecyclerView) v.findViewById(R.id.search_book_rv);
        loading = (ProgressBar) v.findViewById(R.id.loading_search_book_fragment);
        loading.setVisibility(View.VISIBLE);




        searchBookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookAdapter = new BookAdapter(getContext(),bookLists);
        requestQueue = Volley.newRequestQueue(getActivity());
        getBooks();

        System.out.println("sequence 3");
        return v;
    }
}
