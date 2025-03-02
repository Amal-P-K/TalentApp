package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class schedule extends AppCompatActivity {
    ListView lv;
    SharedPreferences sh;
    ArrayList<String>date,ft,tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        lv=findViewById(R.id.lv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        String url ="http://"+sh.getString("ip", "") + ":5000/view_schedule";
        RequestQueue queue = Volley.newRequestQueue(schedule.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    date= new ArrayList<>();
                    ft=new ArrayList<>();
                    tt=new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        ft.add(jo.getString("ft"));
                        tt.add(jo.getString("tt"));
                        date.add(jo.getString("date"));




                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    lv.setAdapter(new custom_sch(schedule.this,date,ft,tt));
//                    l1.setOnItemClickListener(viewuser.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(schedule.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("jid",getIntent().getStringExtra("jid"));

                return params;
            }
        };
        queue.add(stringRequest);
    }
}