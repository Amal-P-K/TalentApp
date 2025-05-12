package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class request extends AppCompatActivity implements AdapterView.OnItemClickListener {
ListView lv;
SharedPreferences sh;
String url;
    ArrayList<String> rid,job,date,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        lv=findViewById(R.id.lv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        String url = "http://10.0.2.2:8000/view_requeststatus";

        String lid = sh.getString("lid", "");
        Log.d("SharedPreferences", "Stored User ID (lid): " + lid);

        if (lid.isEmpty()) {
            Toast.makeText(request.this, "User ID not found. Please login again.", Toast.LENGTH_LONG).show();
            return; // Stop further execution
        }

        RequestQueue queue = Volley.newRequestQueue(request.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("+++++++++++++++++", response);
                try {
                    if (!response.startsWith("[")) {
                        // Handle JSON object response (e.g., error messages)
                        JSONObject errorResponse = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), errorResponse.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONArray ar = new JSONArray(response);
                    rid = new ArrayList<>();
                    job = new ArrayList<>();
                    date = new ArrayList<>();
                    status = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        rid.add(jo.getString("jid"));
                        job.add(jo.getString("job"));
                        date.add(jo.getString("date"));
                        status.add(jo.getString("status"));
                    }

                    lv.setAdapter(new custom_req(request.this, job, status, date));
                    lv.setOnItemClickListener(request.this);

                    if (ar.length() == 0) {
                        Toast.makeText(getApplicationContext(), "No job requests found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String responseData = new String(error.networkResponse.data);
                    Log.e("Server Error", "Error Code: " + error.networkResponse.statusCode + ", Data: " + responseData);
                    Toast.makeText(request.this, "Server Error: " + responseData, Toast.LENGTH_LONG).show();
                } else {
                    Log.e("Network Error", "No response from server: " + error.getMessage());
                    Toast.makeText(request.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }



        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String lid = sh.getString("lid", "");

                if (lid.isEmpty()) {
                    Toast.makeText(request.this, "User ID is missing. Please log in again.", Toast.LENGTH_SHORT).show();
                    return null; // Prevent sending invalid data
                }
                params.put("lid", lid);
                return params;
            }

        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent ik=new Intent(getApplicationContext(),schedule.class);
        ik.putExtra("jid",rid.get(position));
        startActivity(ik);
    }
}