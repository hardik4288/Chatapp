package com.example.user.chitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private TextView profile_name, Status;
    private Button change_image, change_status;
    private DatabaseReference userdatabase;
    private FirebaseUser currentuser;
    private static final int GALLERY_PICK = 1;
    private ProgressDialog progressDialog;
    private StorageReference profile_pic;



    private CircleImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profile_pic = FirebaseStorage.getInstance().getReference();
        profile_name = findViewById(R.id.profile_name);
        change_image = findViewById(R.id.change_image_button);
        change_status = findViewById(R.id.change_status_button);
        Status = findViewById(R.id.status);
        profilepic = findViewById(R.id.profile_pic);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentuser.getUid();
        userdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        userdatabase.keepSynced(true);
        userdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                profile_name.setText(name);
                Status.setText(status);
                if(!image.equals("default")) {
                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.common_google_signin_btn_icon_dark_normal).into(profilepic);
                    Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.user).into(profilepic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.user).into(profilepic);

                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status_value = Status.getText().toString();
                Intent statusintent = new Intent(SettingsActivity.this, StatusActivity.class);
                statusintent.putExtra("status value", status_value);
                startActivity(statusintent);
            }
        });
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryintent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
        Uri imageuri = data.getData();
        CropImage.activity(imageuri)
                .setAspectRatio(1, 1)
                .start(this);


    }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(SettingsActivity.this);
                progressDialog.setTitle("Uploading Image....");
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();



                Uri resultUri = result.getUri();
                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = currentuser.getUid();


                    Bitmap thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
                    final byte[] thumb_byte = baos.toByteArray();




                StorageReference filepath = profile_pic.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = profile_pic.child("profile_images").child("thumbs").child(current_user_id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String download_url = task.getResult().getDownloadUrl().toString();


                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task)
                                {String thumb_download = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()){
                                        Map updatehash = new HashMap();
                                        updatehash.put("image", download_url);
                                        updatehash.put("thumb_image", thumb_download);
                                        userdatabase.updateChildren(updatehash).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"Upload Suceessful",Toast.LENGTH_SHORT).show();

                                                }


                                            }


                                        });


                                    }else {
                                        Toast.makeText(SettingsActivity.this," failed to upload thumbnail",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(SettingsActivity.this,"Error in Uploading",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }




    }

