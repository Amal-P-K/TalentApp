package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class register extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5,e6,e7,e8,e9,photo,rp;
    ImageButton b1;
    SharedPreferences sh;;
    String fn,ln,ph,em,pl,po,pi,usr,pass,gen,rpwd;
    RadioButton rb1,rb2;
    String PathHolder="";
    byte[] filedt=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        e1=findViewById(R.id.fn);
        e2=findViewById(R.id.ln);
        e3=findViewById(R.id.editTextPhone);
        e4=findViewById(R.id.email);
        e5=findViewById(R.id.place);
        e6=findViewById(R.id.post);
        e7=findViewById(R.id.pin);
        e8=findViewById(R.id.un);
        e9=findViewById(R.id.pwd);
        rp=findViewById(R.id.rpwd);
        b1=findViewById(R.id.button);
        photo=findViewById(R.id.photo);
        rb1=findViewById(R.id.radioButton);
        rb2=findViewById(R.id.radioButton2);


        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
//            intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 7);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fn=e1.getText().toString();
                ln=e2.getText().toString();
                ph=e3.getText().toString();
                em=e4.getText().toString();
                pl=e5.getText().toString();
                po=e6.getText().toString();
                pi=e7.getText().toString();
                usr=e8.getText().toString();
                pass=e9.getText().toString();
                rpwd=rp.getText().toString();
                gen="Male";
                if(rb2.isChecked())
                {
                    gen="Female";
                }

                if (fn.equalsIgnoreCase(""))
                {
                    e1.setError("Enter first name");
                    e1.requestFocus();
                }
                else if(!fn.matches("^[a-zA-Z]*$")){
                    e1.setError("characters allowed");
                    e1.requestFocus();
                }
                else if(ln.equalsIgnoreCase(""))
                {
                    e2.setError("Enter last name");
                    e2.requestFocus();
                }
                else if(!ln.matches("^[a-zA-Z]*$")){
                    e2.setError("characters allowed");
                    e2.requestFocus();
                }

                else if(ph.equalsIgnoreCase(""))
                {
                    e3.setError("Enter Your Phone No");
                    e3.requestFocus();
                }
                else if(ph.length() != 10){
                    e3.setError("Minimum 10 nos required");
                    e3.requestFocus();
                }
                else if(!ph.matches("[9876][0-9]{9}$")) {
                    e3.setError("pattern");
                    e3.requestFocus();

                }

                else if(em.equalsIgnoreCase(""))
                {
                    e4.setError("Enter Your Email");
                    e4.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    e4.setError("Enter Valid Email");
                    e4.requestFocus();
                }
                else if(pl.equalsIgnoreCase(""))
                {
                    e5.setError("Enter Your Place");
                    e5.requestFocus();
                }
                else if(!pl.matches("^[a-zA-Z]*$")){
                    e5.setError("characters allowed");
                    e5.requestFocus();
                }
                else if(po.equalsIgnoreCase(""))
                {
                    e6.setError("Enter Your post");
                    e6.requestFocus();
                }
                else if(po.length()!=12)
                {
                    e6.setError("12 numbers required");
                    e6.requestFocus();
                }
//                else if(!po.matches("^[0-9]{12}$")){
//                    e6.setError("invalid");
//                }
                else if(!pi.matches("^[0-9]{2}$")){
                    e7.setError("characters allowed");
                    e7.requestFocus();
                }
                else if(pi.equalsIgnoreCase(""))
                {
                    e7.setError("Enter Your age");
                    e7.requestFocus();
                }
                else if(Integer.parseInt(pi)>26)
                {
                    e7.setError("Age must be less than 26");
                    e7.requestFocus();
                }

                else if(usr.equalsIgnoreCase(""))
                {
                    e8.setError("Enter Your username");
                    e8.requestFocus();
                }
                else if(pass.equalsIgnoreCase(""))
                {
                    e9.setError("Enter Your password");
                    e9.requestFocus();
                }else if(!pass.equals(rpwd))
                {
                    rp.setError("Miss match");
                    rp.requestFocus();
                }



               else
                {
                    uploadBitmap("");
                }
            }
        });

    }

    ProgressDialog pd;
    private void uploadBitmap(final String title) {
        pd=new ProgressDialog(register.this);
        pd.setMessage("Uploading....");
        pd.show();
        String url = "http://10.0.2.2:8000/user_registration";


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response1) {
                        pd.dismiss();
                        String x=new String(response1.data);
                        try {
                            JSONObject obj = new JSONObject(new String(response1.data));
//                        Toast.makeText(Upload_agreement.this, "Report Sent Successfully", Toast.LENGTH_LONG).show();
                            if (obj.getString("task").equalsIgnoreCase("success")) {

                                Toast.makeText(register.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(getApplicationContext(),login.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("firstname", fn);
                params.put("lastname", ln);

                params.put("address", pl);
                params.put("aadhar", po);
                params.put("age", pi);
                params.put("gender", gen);
                params.put("phone", ph);
                params.put("email", em);
                params.put("username", usr);
                params.put("password", pass);
                params.put("lid",sh.getString("lid",""));

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (filedt != null && PathHolder != null && !PathHolder.isEmpty()) {
                    long imagename = System.currentTimeMillis();
                    params.put("file", new DataPart(PathHolder, filedt));
                } else {
                    Log.d("File Upload", "No file selected or file data is null.");
                }
                return params;
            }

        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 7:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("File Uri", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        PathHolder = FileUtils.getPathFromURI(this, uri);
//                        PathHolder = data.getData().getPath();
//                        Toast.makeText(this, PathHolder, Toast.LENGTH_SHORT).show();

                        filedt = getbyteData(PathHolder);
                        Log.d("filedataaa", filedt + "");
//                        Toast.makeText(this, filedt+"", Toast.LENGTH_SHORT).show();
                        photo.setText(PathHolder);
                    }
                    catch (Exception e){
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private byte[] getbyteData(String pathHolder) {
        Log.d("path", pathHolder);
        File fil = new File(pathHolder);
        int fln = (int) fil.length();
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(fil);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[fln];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
            inputStream.close();
        } catch (Exception e) {
        }
        return byteArray;


    }
}