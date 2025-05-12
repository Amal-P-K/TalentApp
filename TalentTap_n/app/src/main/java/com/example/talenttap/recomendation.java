package com.example.talenttap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class recomendation extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<String> jb,jp,details,place,wage,date,jid,pid;
    ListView lv;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendation);
        lv=findViewById(R.id.lv);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        String url ="http://"+sh.getString("ip", "") + ":8000/view_job1/";

        RequestQueue queue = Volley.newRequestQueue(recomendation.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    jb= new ArrayList<>();
                    jp= new ArrayList<>();
                    date= new ArrayList<>();
                    details=new ArrayList<>();
                    wage=new ArrayList<>();
                    place=new ArrayList<>();
                    jid=new ArrayList<>();
                    pid=new ArrayList<>();

                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        jb.add(jo.getString("jb"));
                        jp.add(jo.getString("jp"));
                        date.add(jo.getString("date"));
                        details.add(jo.getString("det"));
                        wage.add(jo.getString("wage"));
                        place.add(jo.getString("place"));
                        jid.add(jo.getString("jid"));
                        pid.add(jo.getString("pid"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    lv.setAdapter(new custom_job(recomendation.this,jb,details,place,wage,date,jp));
                    lv.setOnItemClickListener(recomendation.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(recomendation.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid",sh.getString("lid",""));

                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder ald=new AlertDialog.Builder(recomendation.this);
        ald.setTitle(jb.get(position))
                .setPositiveButton(" Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close the dialog without opening any activity
                    }
                })

                .setNegativeButton(" Send request ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        RequestQueue queue = Volley.newRequestQueue(recomendation.this);
                        String url = "http://" + sh.getString("ip", "") + ":8000/snd_request";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response here
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors here
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("lid", sh.getString("lid",""));
                                params.put("jid", jid.get(position)); // Send Job ID
                                return params;
                            }
                        };
                        queue.add(stringRequest);
                    }
                });


        AlertDialog al=ald.create();
        al.show();

    }

    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), home.class);
        startActivity(ik);
    }
}