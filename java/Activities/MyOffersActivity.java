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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MyOffersActivity extends AppCompatActivity {

    private String userId;

    private ArrayList<String> postIdFromFB;
    private ArrayList<String> offerIdFromFB;
    private ArrayList<String> imageUrlFromFB;
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
    private ArrayList<String> offerStatusFromFB;
    private ArrayList<String> workDoneFromFB;

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;

    private MyOffersRecycleViewAdapter myOffersRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);

        getSupportActionBar().setTitle("My Offers");

        final BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewMyOffers);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        Intent homeIntent = new Intent(MyOffersActivity.this,PostActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.add_post:
                        Intent selectLocationIntent = new Intent(MyOffersActivity.this,SelectLocationActivity.class);
                        startActivity(selectLocationIntent);
                        break;
                    case R.id.profile:
                        finish();
                        Intent profileIntent = new Intent(MyOffersActivity.this, MySettingsActivity.class);
                        startActivity(profileIntent);
                        break;
                }

                return true;
            }
        });

        postIdFromFB = new ArrayList<>();
        offerIdFromFB = new ArrayList<>();
        imageUrlFromFB = new ArrayList<>();
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
        offerStatusFromFB = new ArrayList<>();
        workDoneFromFB = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            userId = user.getUid();
        }

        firebaseFirestore = FirebaseFirestore.getInstance();

        getMyOffersFromDatabase();

        RecyclerView myOffersRecycleView = findViewById(R.id.recyclerViewMyOffers);
        myOffersRecycleView.setLayoutManager(new LinearLayoutManager(this));
        myOffersRecycleViewAdapter = new MyOffersRecycleViewAdapter(postIdFromFB ,offerIdFromFB , workDoneFromFB , imageUrlFromFB,titleFromFB,categoryFromFB,explanationFromFB,workDateFromFB,startTimeFromFB,finishTimeFromFB,paymentFromFB,addressFromFB,latitudeFromFB,longitudeFromFB,offerStatusFromFB);
        myOffersRecycleView.setAdapter(myOffersRecycleViewAdapter);

    }

    public void getMyOffersFromDatabase() {

        final CollectionReference postsOfferReference = firebaseFirestore.collection("Posts_Offer");
      //  final CollectionReference workPostsReference = firebaseFirestore.collection("WorkPosts");

        postsOfferReference.whereEqualTo("user_id",userId).orderBy("create_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(MyOffersActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if (value != null){

                    postIdFromFB.clear();
                    offerIdFromFB.clear();
                    imageUrlFromFB.clear();
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
                    offerStatusFromFB.clear();
                    workDoneFromFB.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){

                        final String offerId = snapshot.getId();

                        final Map<String,Object> data = snapshot.getData();

                        final String postId = (String) data.get("post_id");
                        final String offerStatus = (String) data.get("offer_status");
                        final String payment = (String) data.get("payment");
                        final String workDone = (String) data.get("work_done");

                        final DocumentReference workPostDocumentReference = firebaseFirestore.collection("WorkPosts").document(postId);
                        workPostDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){

                                    final DocumentSnapshot document = task.getResult();
                                    if (document.exists()){

                                        final Map<String,Object> postData = document.getData();

                                        final String imageUrl = (String) postData.get("image_url");
                                        final String title = (String) postData.get("title");
                                        final String category = (String) postData.get("category");
                                        final String address = (String) postData.get("address");
                                        final String explanation = (String) postData.get("explanation");
                                        final String date = (String) postData.get("date");
                                        final String startTime = (String) postData.get("start_time");
                                        final String finishTime = (String) postData.get("finish_time");
                                        final String latitude = (String) postData.get("latitude");
                                        final String longitude = (String) postData.get("longitude");

                                        // Arraylere eklenecek
                                        postIdFromFB.add(postId);
                                        offerIdFromFB.add(offerId);

                                        System.out.println("OFFER ID ID ID  : " + offerId );
                                        imageUrlFromFB.add(imageUrl);
                                        titleFromFB.add(title);
                                        categoryFromFB.add(category);
                                        explanationFromFB.add(explanation);
                                        workDateFromFB.add(date);
                                        startTimeFromFB.add(startTime);
                                        finishTimeFromFB.add(finishTime);
                                        addressFromFB.add(address);
                                        latitudeFromFB.add(latitude);
                                        longitudeFromFB.add(longitude);
                                        paymentFromFB.add(payment);
                                        offerStatusFromFB.add(offerStatus);
                                        workDoneFromFB.add(workDone);

                                        myOffersRecycleViewAdapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        });


                    }

                }

            }
        });

    }



}