package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MyPostsActivity extends AppCompatActivity implements MyPostsRecycleViewAdapter.OnMyPostsListener{

    private String userId;
    private ArrayList<String> postIdFromDB;
    private ArrayList<String> imagesFromFB;
    private ArrayList<String> titleFromFB;
    private ArrayList<String> categoryFromFB;
    private ArrayList<String> explanationFromFB;
    private ArrayList<String> workDateFromFB;
    private ArrayList<String> startTimeFromFB;
    private ArrayList<String> finishTimeFromFB;
    private ArrayList<String> addressFromFB;
    private ArrayList<String> latitudeFromFB;
    private ArrayList<String> longitudeFromFB;
    private ArrayList<String> paymentFromFB;

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;

    private MyPostsRecycleViewAdapter myPostsRecycleViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        getSupportActionBar().setTitle("My Posts");

        final BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewMyPosts);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        Intent homeIntent = new Intent(MyPostsActivity.this,PostActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.add_post:
                        Intent selectLocationIntent = new Intent(MyPostsActivity.this,SelectLocationActivity.class);
                        startActivity(selectLocationIntent);
                        break;
                    case R.id.profile:
                        finish();
                        Intent profileIntent = new Intent(MyPostsActivity.this, MySettingsActivity.class);
                        startActivity(profileIntent);
                        break;
                }

                return true;
            }
        });


        postIdFromDB = new ArrayList<>();
        imagesFromFB = new ArrayList<>();
        titleFromFB = new ArrayList<>();
        categoryFromFB = new ArrayList<>();
        explanationFromFB = new ArrayList<>();
        workDateFromFB = new ArrayList<>();
        startTimeFromFB = new ArrayList<>();
        finishTimeFromFB = new ArrayList<>();
        addressFromFB = new ArrayList<>();
        latitudeFromFB = new ArrayList<>();
        longitudeFromFB = new ArrayList<>();
        paymentFromFB = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            userId = user.getUid();
        }

        firebaseFirestore = FirebaseFirestore.getInstance();


        getMyPostsFromDatabase();
        //My Posts Recycle View
        RecyclerView myPostsRecycleView = findViewById(R.id.recycleViewMyPosts);
        myPostsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        myPostsRecycleViewAdapter = new MyPostsRecycleViewAdapter(explanationFromFB,addressFromFB,latitudeFromFB,longitudeFromFB, postIdFromDB,imagesFromFB,titleFromFB,categoryFromFB,workDateFromFB,startTimeFromFB,finishTimeFromFB,paymentFromFB, this);
        myPostsRecycleView.setAdapter(myPostsRecycleViewAdapter);

    }



    public void getMyPostsFromDatabase() {
        // Filtreleme yapmak istersen
        // workPostsReference.whereEqualTo("category","Pet").addSnapshotListener(.......)

        final CollectionReference workPostsReference = firebaseFirestore.collection("WorkPosts");

        workPostsReference.whereEqualTo("user_id",userId).orderBy("create_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(MyPostsActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if(value != null){
                    postIdFromDB.clear();
                    imagesFromFB.clear();
                    titleFromFB.clear();
                    categoryFromFB.clear();
                    explanationFromFB.clear();
                    workDateFromFB.clear();
                    startTimeFromFB.clear();
                    finishTimeFromFB.clear();
                    addressFromFB.clear();
                    latitudeFromFB.clear();
                    longitudeFromFB.clear();
                    paymentFromFB.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){

                        final Map<String,Object> data = snapshot.getData();

                        final String imageUrl = (String) data.get("image_url");
                        final String title = (String) data.get("title");
                        final String category = (String) data.get("category");
                        final String explanation = (String) data.get("explanation");
                        final String date = (String) data.get("date");
                        final String start_time = (String) data.get("start_time");
                        final String finish_time = (String) data.get("finish_time");
                        final String address = (String) data.get("address");
                        final String latitude = (String) data.get("latitude");
                        final String longitude = (String) data.get("longitude");
                        final String payment = (String) data.get("payment");

                        postIdFromDB.add(snapshot.getId());
                        imagesFromFB.add(imageUrl);
                        titleFromFB.add(title);
                        categoryFromFB.add(category);
                        explanationFromFB.add(explanation);
                        workDateFromFB.add(date);
                        startTimeFromFB.add(start_time);
                        finishTimeFromFB.add(finish_time);
                        addressFromFB.add(address);
                        latitudeFromFB.add(latitude);
                        longitudeFromFB.add(longitude);
                        paymentFromFB.add(payment);

                        myPostsRecycleViewAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    @Override
    public void onMyPostsClick(int position) {
        
        final String postId = postIdFromDB.get(position);
        final String payment = paymentFromFB.get(position);
        final String title = titleFromFB.get(position);

        final Intent myPostsOffersIntent = new Intent(MyPostsActivity.this, PostsOffersActivity.class);
        myPostsOffersIntent.putExtra("post_id",postId);
        myPostsOffersIntent.putExtra("payment",payment);
        myPostsOffersIntent.putExtra("title",title);
        startActivity(myPostsOffersIntent);
    }
}