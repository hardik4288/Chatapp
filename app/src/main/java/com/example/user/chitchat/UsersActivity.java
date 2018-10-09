package com.example.user.chitchat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersActivity extends AppCompatActivity {
     private Toolbar toolbar;
     private DatabaseReference usersdatabase;
     private RecyclerView userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_users);
        userlist = findViewById(R.id.userslist);
        toolbar = findViewById(R.id.user_appbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        usersdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userlist.setHasFixedSize(true);
        userlist.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.single_user,
                UserViewHolder.class,
                usersdatabase




        ) {





            @Override
            protected void populateViewHolder (UserViewHolder viewHolder, Users users,int position){
                viewHolder.setName(users.getName());
                viewHolder.setUserStatus(users.getStatus());
                viewHolder.setUserImage(users.getThumb_image(),getApplicationContext());
                final String user_id = getRef(position).getKey();

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileintent = new Intent(UsersActivity.this,ProfileActivity.class);
                        profileintent.putExtra("user_id", user_id);
                        startActivity(profileintent);
                    }
                });


            }
        };
        userlist.setAdapter(firebaseRecyclerAdapter);

    }
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View view;

        public UserViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

    public void setName(String name) {
        TextView usernameview = view.findViewById(R.id.user_singleuser);
        usernameview.setText(name);
    }
        public void setUserStatus(String status){
            TextView userstatus = view.findViewById(R.id.status_single_user);
            userstatus.setText((status));
        }
        public void setUserImage(String thumb_image , Context ctx){
            CircleImageView userimageview = view.findViewById(R.id.single_display_photo);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.user).into(userimageview);
        }

    }


}
