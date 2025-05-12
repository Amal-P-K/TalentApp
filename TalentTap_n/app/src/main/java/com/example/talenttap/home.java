package com.example.talenttap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Locale;

public class home extends AppCompatActivity {
    CardView b1,b2,b3,b4,b5,b6,cb;
    SharedPreferences sh;
    RelativeLayout topArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        b1=findViewById(R.id.myplaylist);
        b2=findViewById(R.id.music);
        b3=findViewById(R.id.dd);
        b4=findViewById(R.id.logout);
        b5=findViewById(R.id.comp);
        b6=findViewById(R.id.feedbk);
        cb=findViewById(R.id.cb);
        topArea=findViewById(R.id.topArea);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            java.net.URL thumb_u = null;
            String imgPath = sh.getString("img", "");
            if (imgPath != null && !imgPath.isEmpty()) {
                thumb_u = new java.net.URL("http://" + sh.getString("ip", "") + ":8000" + imgPath);
                Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
                topArea.setBackground(thumb_d);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error, but don’t block the login
        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), job.class);
                startActivity(i);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), request.class);
                startActivity(i);

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), complaint.class);
                startActivity(i);

            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), login.class);
                startActivity(i);

            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), skill.class);
                startActivity(i);

            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), recomendation.class);
                startActivity(i);

            }
        });  cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Test.class);
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Would you like to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      Intent ik=new Intent(getApplicationContext(),login.class);
                      startActivity(ik);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesn't want to logout
                    }
                })
                .show();
    }
}