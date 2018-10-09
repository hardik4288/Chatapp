package com.example.user.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText name,pwd,mail;
    private ProgressDialog regprog;
    Button createbtn;
    private DatabaseReference database;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regprog = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        pwd =  findViewById(R.id.pwd);
        mail =  findViewById(R.id.mail);
        createbtn =  findViewById(R.id.createbtn);


        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = name.getEditableText().toString();
                String email = mail.getEditableText().toString();
                String pass = pwd.getEditableText().toString();
                if (!TextUtils.isEmpty(display_name) || !(TextUtils.isEmpty(email)) ||!(TextUtils.isEmpty(pass))   ){
                register_user(display_name, email, pass);
                regprog.setTitle("Registering user");
                regprog.setMessage("Please wait While your Account is creating");
                regprog.show();
                regprog.setCanceledOnTouchOutside(false);




                }
            }
        });
    }

    private void register_user(final String display_name, String email, String pass) {




        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_user.getUid();
                            database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String , String> userMap = new HashMap<>();
                            userMap.put("name",display_name);
                            userMap.put("status","Hello i'm using Chitchat");
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            database.setValue(userMap);





                            regprog.dismiss();

                            Intent mainIntent = new Intent(RegisterActivity.this,StartActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            regprog.hide();
                            Toast.makeText(RegisterActivity.this, "Sign up failed",Toast.LENGTH_SHORT).show();

                        // ...
                    }
                }
    });
}
}
