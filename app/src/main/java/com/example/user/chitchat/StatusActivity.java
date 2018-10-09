package com.example.user.chitchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StatusActivity extends AppCompatActivity {
    Toolbar toolbar;
    private FirebaseUser currentuser;
    private ProgressDialog progress;
    private DatabaseReference statusdatabase;
EditText changestatus;
Button statusbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        toolbar= findViewById(R.id.status_appbar);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentuser.getUid();
        statusdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        setSupportActionBar(toolbar);
        String status_value = getIntent().getStringExtra("status_value");
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changestatus = findViewById(R.id.change_status_text);
        statusbutton = findViewById(R.id.change_status);



        changestatus.setText(status_value);
        statusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = changestatus.getEditableText().toString();
                progress = new ProgressDialog(StatusActivity.this);
                progress.setTitle("Saving Changes");
                progress.setMessage("Please wait");
                progress.show();

                statusdatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progress.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(),"Operation Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });



    }
}
