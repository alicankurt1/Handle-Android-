package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MySettingsActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);

        getSupportActionBar().setTitle("Profile");

        final BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewMySettings);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        Intent homeIntent = new Intent(MySettingsActivity.this,PostActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.add_post:
                        Intent selectLocationIntent = new Intent(MySettingsActivity.this,SelectLocationActivity.class);
                        startActivity(selectLocationIntent);
                        break;
                    case R.id.profile:
                        finish();
                        startActivity(getIntent());
                        break;
                }

                return true;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }



    public void accountDetailButton (View view){
        Intent profileIntent = new Intent(MySettingsActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void myPostsButton (View view){
        Intent myPostsIntent = new Intent(MySettingsActivity.this, MyPostsActivity.class);
        startActivity(myPostsIntent);
    }

    public void myOffersButton (View view){
        Intent myOffersIntent = new Intent(MySettingsActivity.this, MyOffersActivity.class);
        startActivity(myOffersIntent);
    }

    public void logoutButton (View view){
        firebaseAuth.signOut();
        Intent loginIntent = new Intent(MySettingsActivity.this,MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }





}


