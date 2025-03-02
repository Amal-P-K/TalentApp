package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class sendcomp extends AppCompatActivity {
EditText editTextTextPersonName2;
Button b3;
SharedPreferences sh;
String comp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendcomp);
        editTextTextPersonName2=findViewById(R.id.editTextTextPersonName2);
        b3=findViewById(R.id.button3);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comp=editTextTextPersonName2.getText().toString();
                if(comp.equalsIgnoreCase(""))
                {
                    editTextTextPersonName2.setError("Missing");
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(sendcomp.this);
                  String  url = "http://" + sh.getString("ip", "") + ":5000/snd_complaints";

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

                                    Intent ik = new Intent(getApplicationContext(), complaint.class);
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(sendcomp.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(sendcomp.this, "=="+e, Toast.LENGTH_SHORT).show();

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
                            params.put("pid", getIntent().getStringExtra("pid"));
                            params.put("comp", comp);

                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });


    }
}