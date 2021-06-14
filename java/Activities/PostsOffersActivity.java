package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class PostsOffersActivity extends AppCompatActivity {

    private String postTitle,postId,userOffer;

    private ArrayList<String> userInfoIdFromFB;
    private ArrayList<String> offerIdFromFB;
    private ArrayList<String> avatarUrlFromFB;
    private ArrayList<String> userNameFromFB;
    private ArrayList<String> userSurnameFromFB;
    private ArrayList<String> userPhoneNumberFromFB;
    private ArrayList<String> userBirthdayFromFB;
    private ArrayList<String> userRatingFromFB;
    private ArrayList<String> usersOfferFromFB;
    private ArrayList<String> offerStatusFromFB;
    private ArrayList<String> workDoneFromFB;
    private ArrayList<String> givingRatingFromFB;

    private FirebaseFirestore myPostsFirebaseFirestore;
    private FirebaseFirestore postsOfferFirebaseFirestore;

    private PostsOffersRecycleViewAdapter postsOffersRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_offers);

        Intent valuesIntent = getIntent();
        postId = valuesIntent.getStringExtra("post_id");
        postTitle = valuesIntent.getStringExtra("title");
        //offer = valuesIntent.getStringExtra("payment");

        getSupportActionBar().setTitle("Offers for " + postTitle);

        offerIdFromFB = new ArrayList<>();
        avatarUrlFromFB = new ArrayList<>();
        userNameFromFB = new ArrayList<>();
        userSurnameFromFB = new ArrayList<>();
        userPhoneNumberFromFB = new ArrayList<>();
        userBirthdayFromFB = new ArrayList<>();
        userRatingFromFB = new ArrayList<>();
        usersOfferFromFB = new ArrayList<>();
        offerStatusFromFB = new ArrayList<>();
        workDoneFromFB = new ArrayList<>();
        givingRatingFromFB = new ArrayList<>();
        userInfoIdFromFB = new ArrayList<>();

        myPostsFirebaseFirestore = FirebaseFirestore.getInstance();
        postsOfferFirebaseFirestore = FirebaseFirestore.getInstance();

        getMyPostsOffersFromDatabase();

        RecyclerView postsOffersRecycleView = findViewById(R.id.recyclerViewPostsOffers);
        postsOffersRecycleView.setLayoutManager(new LinearLayoutManager(this));
        postsOffersRecycleViewAdapter = new PostsOffersRecycleViewAdapter( userInfoIdFromFB,workDoneFromFB,givingRatingFromFB ,avatarUrlFromFB,userNameFromFB,userSurnameFromFB,userPhoneNumberFromFB,userBirthdayFromFB,userRatingFromFB,usersOfferFromFB,offerIdFromFB,offerStatusFromFB,postId,postTitle);
        postsOffersRecycleView.setAdapter(postsOffersRecycleViewAdapter);

    }

    public void getMyPostsOffersFromDatabase(){

        // Filtreleme yapmak istersen
        // workPostsReference.whereEqualTo("category","Pet").addSnapshotListener(.......)

        final CollectionReference postsOffersReference = myPostsFirebaseFirestore.collection("Posts_Offer");
        final CollectionReference userInfoReference = postsOfferFirebaseFirestore.collection("UserInformations");

        postsOffersReference.whereEqualTo("post_id",postId).whereNotEqualTo("offer_status","declined").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(PostsOffersActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if(value != null){

                    userInfoIdFromFB.clear();
                    offerIdFromFB.clear();
                    avatarUrlFromFB.clear();
                    userNameFromFB.clear();
                    userSurnameFromFB.clear();
                    userPhoneNumberFromFB.clear();
                    userBirthdayFromFB.clear();
                    userRatingFromFB.clear();
                    usersOfferFromFB.clear();
                    offerStatusFromFB.clear();
                    workDoneFromFB.clear();
                    givingRatingFromFB.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object> data = snapshot.getData();
                        String offerId = snapshot.getId();
                        String userId = (String) data.get("user_id");
                        String offer = (String) data.get("payment");
                        String offerStatus = (String) data.get("offer_status");
                        String workDone = (String) data.get("work_done");
                        String givingRating = (String) data.get("giving_rating");

                        userInfoReference.whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        final Map<String,Object> userData = document.getData();
                                        final String userInfoId = document.getId();
                                        final String avatarUrl = (String) userData.get("image_url");
                                        final String userName = (String) userData.get("name");
                                        final String userSurname = (String) userData.get("surname");
                                        final String userPhoneNumber = (String) userData.get("phone_number");
                                        final String userBirthday = (String) userData.get("birthday");
                                        final String userRating = (String) userData.get("rating");

                                        userInfoIdFromFB.add(userInfoId);
                                        offerIdFromFB.add(offerId);
                                        avatarUrlFromFB.add(avatarUrl);
                                        userNameFromFB.add(userName);
                                        userSurnameFromFB.add(userSurname);
                                        userPhoneNumberFromFB.add(userPhoneNumber);
                                        userBirthdayFromFB.add(userBirthday);
                                        userRatingFromFB.add(userRating);
                                        usersOfferFromFB.add(offer);
                                        offerStatusFromFB.add(offerStatus);
                                        workDoneFromFB.add(workDone);
                                        givingRatingFromFB.add(givingRating);

                                        postsOffersRecycleViewAdapter.notifyDataSetChanged();

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