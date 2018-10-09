package com.example.user.chitchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;

    private ViewPager viewpager;
    private TabLayout tabLayout;
    private DatabaseReference mUserRef;

    private PagerAdapter pageradapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
/*
        copy na karna made by hardik ---_____----
        declare
        */
        mAuth = FirebaseAuth.getInstance();


        mtoolbar =  findViewById(R.id.main_page_toobar);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Chit chat");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewpager =  findViewById(R.id.main_tab_pager);
        tabLayout = findViewById(R.id.tab_layout);
        pageradapter = new PagerAdapter(getSupportFragmentManager());

        viewpager.setAdapter(pageradapter);
        tabLayout.setupWithViewPager(viewpager);


       //getSupportActionBar().setTitle("Chit chat");


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {

        sendToStart();

        }else {
            mUserRef.child("online").setValue(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUserRef.child("online").setValue(false);
    }

    private void sendToStart() {


            Intent startIntent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);





         return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() ==  R.id.main_logout) {

                FirebaseAuth.getInstance().signOut();
                sendToStart();

        }
        if(item.getItemId() == R.id.main_settings)
        {
            Intent settingsintent = new Intent(StartActivity.this , SettingsActivity.class);
            startActivity(settingsintent);

        }

        if (item.getItemId() == R.id.main_all_users)
        {
            Intent userintent = new Intent(StartActivity.this, UsersActivity.class);
            startActivity(userintent);
        }
        return true;
    }
}
