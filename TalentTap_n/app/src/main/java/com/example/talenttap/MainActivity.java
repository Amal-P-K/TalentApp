package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    Button b1;
    String ip;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=findViewById(R.id.editTextTextPersonName);
        b1=findViewById(R.id.button);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1.setText(sh.getString("ip",""));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip=e1.getText().toString();

                if (ip.equalsIgnoreCase("")){
                    e1.setError("Enter your IP address");
                }
                else{
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("ip",ip);
                    ed.commit();

                    Intent i1 = new Intent(getApplicationContext(), login.class);
                    startActivity(i1);
                    Toast.makeText(MainActivity.this, ip+" : please login", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }
}