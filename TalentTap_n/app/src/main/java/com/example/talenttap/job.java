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
    ArrayList<String> jb, jp, details, place, wage, date, jid, pid;
    ListView lv;
    SharedPreferences sh;
    Button b1;
    EditText e1;
    String jobb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        lv = findViewById(R.id.lv);
        b1 = findViewById(R.id.button2);
        e1 = findViewById(R.id.fn);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        fetchAllJobs();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobb = e1.getText().toString();
                if (jobb.isEmpty()) {
                    Toast.makeText(job.this, "Please enter a job name", Toast.LENGTH_SHORT).show();
                } else {
                    fetchFilteredJobs(jobb);
                }
            }
        });
    }

    private void fetchAllJobs() {
        sendJobRequest("/view_job", new HashMap<>());
    }

    private void fetchFilteredJobs(String jobName) {
        Map<String, String> params = new HashMap<>();
        params.put("job", jobName);
        sendJobRequest("/view_job2", params);
    }

    private void sendJobRequest(String endpoint, Map<String, String> params) {
        String url = "http://" + sh.getString("ip", "") + ":8000" + endpoint;
        RequestQueue queue = Volley.newRequestQueue(job.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> handleJobResponse(response),
                error -> handleError(error)) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void handleJobResponse(String response) {
        try {
            JSONArray ar = new JSONArray(response);
            jb = new ArrayList<>();
            jp = new ArrayList<>();
            date = new ArrayList<>();
            details = new ArrayList<>();
            wage = new ArrayList<>();
            place = new ArrayList<>();
            jid = new ArrayList<>();
            pid = new ArrayList<>();
            ArrayList<String> company = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject jo = ar.getJSONObject(i);
                jb.add(jo.getString("jb"));
                jp.add(jo.getString("jp"));
                date.add(jo.getString("date"));
                details.add(jo.getString("det"));
                wage.add(jo.getString("wage"));
                place.add(jo.getString("place"));
                jid.add(jo.getString("jid"));
                pid.add(jo.getString("pid"));
            }

            lv.setAdapter(new custom_job(job.this, jb, details, place, wage, date, jp));
            lv.setOnItemClickListener(job.this);
        } catch (JSONException e) {
            Log.e("Job Response", "JSON parsing error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(VolleyError error) {
        Log.e("Volley Error", error.toString());
        Toast.makeText(job.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder ald = new AlertDialog.Builder(job.this);
        ald.setTitle(jb.get(position))
                .setPositiveButton("Send Complaint", (arg0, arg1) -> {
                    Intent ik = new Intent(getApplicationContext(), sendcomp.class);
                    ik.putExtra("pid", pid.get(position));
                    startActivity(ik);
                })
                .setNegativeButton("Send Request", (arg0, arg1) -> sendJobRequest(position))
                .create()
                .show();
    }

    private void sendJobRequest(int position) {
        String url = "http://" + sh.getString("ip", "") + ":8000/snd_request";
        RequestQueue queue = Volley.newRequestQueue(job.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> handleSendRequestResponse(response),
                error -> handleError(error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("lid", ""));
                params.put("jid", jid.get(position));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void handleSendRequestResponse(String response) {
        Log.d("Server Response", response);
        try {
            JSONObject json = new JSONObject(response);
            String task = json.optString("task", "unknown");

            if ("valid".equalsIgnoreCase(task)) {
                Toast.makeText(job.this, "Request sent successfully", Toast.LENGTH_SHORT).show();
                fetchAllJobs();
            } else {
                Toast.makeText(job.this, "Failed to send request: " + json.optString("message", "Unknown Error"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(job.this, "Response parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), home.class);
        startActivity(ik);
    }
}
