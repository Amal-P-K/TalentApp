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
import android.widget.Button;
import android.widget.EditText;
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
public class job extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<String> jb,jp,details,place,wage,date,jid,pid;
    ListView lv;
    SharedPreferences sh;
    Button b1;
    EditText e1;
    String jobb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        lv=findViewById(R.id.lv);
        b1=findViewById(R.id.button2);
        e1=findViewById(R.id.fn);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobb=e1.getText().toString();
                String  url ="http://"+sh.getString("ip", "") + ":5000/view_job2";
                RequestQueue queue = Volley.newRequestQueue(job.this);

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

                            lv.setAdapter(new custom_job(job.this,jb,details,place,wage,date,jp));
                            lv.setOnItemClickListener(job.this);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                            Log.d("=========", e.toString());
                        }


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(job.this, "err"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("job",jobb);
                        return params;
                    }
                };
                queue.add(stringRequest);

            }
        });


      String  url ="http://"+sh.getString("ip", "") + ":5000/view_job";
        RequestQueue queue = Volley.newRequestQueue(job.this);

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

                    lv.setAdapter(new custom_job(job.this,jb,details,place,wage,date,jp));
                    lv.setOnItemClickListener(job.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(job.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("lid",sh.getString("lid",""));

                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder ald=new AlertDialog.Builder(job.this);
        ald.setTitle(jb.get(position))
                .setPositiveButton(" Send Complaint ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try
                        {

                            Intent ik=new Intent(getApplicationContext(),sendcomp.class);
                            ik.putExtra("pid",pid.get(position));
                            startActivity(ik);


                        }
                        catch(Exception e)
                        {
                            Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(" Send request ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        RequestQueue queue = Volley.newRequestQueue(job.this);
                     String   url = "http://" + sh.getString("ip", "") + ":5000/snd_request";

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                Log.d("+++++++++++++++++", response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    String res = json.getString("task");

                                    if (res.equalsIgnoreCase("valid")) {
                                        Toast.makeText(job.this, "Success", Toast.LENGTH_SHORT).show();

                                        Intent ik = new Intent(getApplicationContext(), job.class);
                                        startActivity(ik);

                                    } else {

                                        Toast.makeText(job.this, "Invalid", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(job.this, "=="+e, Toast.LENGTH_SHORT).show();

                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                                Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("lid", sh.getString("lid",""));
                                params.put("jid", jid.get(position));

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