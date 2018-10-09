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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
private EditText emaillogin,passlogin;
private Button btnlogin;
private ProgressDialog loginprogress;
private DatabaseReference userdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emaillogin =  findViewById(R.id.emaillogin);
        passlogin =  findViewById(R.id.passlogin);
        userdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        btnlogin =  findViewById(R.id.btnlogin);
        loginprogress = new ProgressDialog(this);

        btnlogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emaillogin.getEditableText().toString();
                String pass = passlogin.getEditableText().toString();
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass));
                {
                    loginprogress.setTitle("Logging in");
                    loginprogress.setMessage("Please wait");
                    loginprogress.show();
                    loginprogress.setCanceledOnTouchOutside(false);
                    loginusr(email, pass);
                }
            }
        });


    }

    private void loginusr(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginprogress.dismiss();
                    String devicetoken = FirebaseInstanceId.getInstance().getToken();
                    String current_user = mAuth.getCurrentUser().getUid();
                    userdatabase.child(current_user).child("device_token").setValue(devicetoken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent loginintent = new Intent(LoginActivity.this,StartActivity.class);
                            loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginintent);
                            finish();


                        }
                    });



                }else {
                    loginprogress.hide();
                    Toast.makeText(LoginActivity.this,"Email or Password is Wrong",Toast.LENGTH_SHORT).show();

                }
            }
        });











    }
}
