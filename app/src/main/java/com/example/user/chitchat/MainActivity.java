package com.example.user.chitchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button regbtn,loginbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regbtn = (Button) findViewById(R.id.regbtn);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(regIntent);

            }
        });
      loginbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent logintent = new Intent(MainActivity.this,LoginActivity.class);
              startActivity(logintent);


          }
      });





    }





}