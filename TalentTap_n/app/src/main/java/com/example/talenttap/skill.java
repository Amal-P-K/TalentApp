package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class skill extends AppCompatActivity implements AdapterView.OnItemClickListener {
ListView lv;
Button b2;
EditText fn;
SharedPreferences sh;
    ArrayList<String> sid,skl;
    String skil="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        lv=findViewById(R.id.lv);
        b2=findViewById(R.id.button2);
        fn=findViewById(R.id.fn);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


      String  url ="http://"+sh.getString("ip", "") + ":5000/skill";
        RequestQueue queue = Volley.newRequestQueue(skill.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    sid= new ArrayList<>();
                    skl= new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        sid.add(jo.getString("sid"));
                        skl.add(jo.getString("skl"));



                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<>(skill.this,android.R.layout.simple_list_item_1,skl);
                    lv.setAdapter(ad);

//                    lv.setAdapter(new custom_pl(skill.this,file,musician,song,mid,details,genre,emotion));
                    lv.setOnItemClickListener(skill.this);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(skill.this, "err"+error, Toast.LENGTH_SHORT).show();
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


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                skil=fn.getText().toString();
                if(skil.equalsIgnoreCase(""))
                {
                    fn.setError("Missing");
                }
                else {

                    RequestQueue queue = Volley.newRequestQueue(skill.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/insert_skill";

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

                                    Intent ik = new Intent(getApplicationContext(), skill.class);
                                    startActivity(ik);


                                } else {

                                    Toast.makeText(skill.this, "Error", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(skill.this, "==" + e, Toast.LENGTH_SHORT).show();

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
                            params.put("lid", sh.getString("lid", ""));
                            params.put("skl", skil);

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RequestQueue queue = Volley.newRequestQueue(skill.this);
        String url = "http://" + sh.getString("ip", "") + ":5000/delete_skill";

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

                        Intent ik = new Intent(getApplicationContext(), skill.class);
                        startActivity(ik);


                    } else {

                        Toast.makeText(skill.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(skill.this, "==" + e, Toast.LENGTH_SHORT).show();

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
                params.put("sid", sid.get(position));

                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), home.class);
        startActivity(ik);
    }
}