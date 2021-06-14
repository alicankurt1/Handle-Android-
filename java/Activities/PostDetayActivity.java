package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostDetayActivity extends AppCompatActivity {

    private String title, postId, payment, imageUrl, category, explanation, workDate, startTime, finishTime, address, latitude, longitude, userId;
    private ImageView imageViewDetailImage;
    private TextView textViewDetailTitle, textViewDetailExplanation, textViewDetailCategory, textViewDetailStartTime, textViewDetailFinishTime, textViewDetailAddress, textViewDetailDate, textViewDetailPayment;
    private TextInputLayout editTextDetailOffer;
    private Button buttonDetailOffer, buttonDetailOfferUpdate, buttonDetailAccept, buttonDetailUpdatePayment;

    private String postOfferId, oldOffer;

    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detay);

        getSupportActionBar().setTitle("Details");

        imageViewDetailImage = findViewById(R.id.imageViewDetailImage);

        textViewDetailTitle = findViewById(R.id.textViewDetailTitle);
        textViewDetailCategory = findViewById(R.id.textViewDetailCategory);
        textViewDetailExplanation = findViewById(R.id.textViewDetailExplanation);
        textViewDetailAddress = findViewById(R.id.textViewDetailAddress);
        textViewDetailDate = findViewById(R.id.textViewDetailDate);
        textViewDetailStartTime = findViewById(R.id.textViewDetailStartTime);
        textViewDetailFinishTime = findViewById(R.id.textViewDetailFinishTime);
        textViewDetailPayment = findViewById(R.id.textViewDetailPayment);

        editTextDetailOffer = findViewById(R.id.editTextDetailOffer);

        // buttons
        buttonDetailOffer = findViewById(R.id.buttonDetailOffer);
        buttonDetailAccept = findViewById(R.id.buttonDetailAccept);
        buttonDetailOfferUpdate = findViewById(R.id.buttonDetailOffer);
        buttonDetailUpdatePayment = findViewById(R.id.buttonDetailUpdatePayment);

        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            System.out.println("UID :" + userId);
        }

        final Intent valuesIntent = getIntent();

        postId = valuesIntent.getStringExtra("post_id");
        imageUrl = valuesIntent.getStringExtra("image_url");
        title = valuesIntent.getStringExtra("title");
        category = valuesIntent.getStringExtra("category");
        explanation = valuesIntent.getStringExtra("explanation");
        workDate = valuesIntent.getStringExtra("work_date");
        startTime = valuesIntent.getStringExtra("start_time");
        finishTime = valuesIntent.getStringExtra("finish_time");
        address = valuesIntent.getStringExtra("address");
        latitude = valuesIntent.getStringExtra("latitude");
        longitude = valuesIntent.getStringExtra("longitude");
        payment = valuesIntent.getStringExtra("payment");

        Picasso.get().load(imageUrl).into(imageViewDetailImage);
        textViewDetailTitle.setText(title);
        textViewDetailCategory.setText(category);
        textViewDetailExplanation.setText(explanation);
        textViewDetailAddress.setText(address);
        textViewDetailDate.setText(workDate);
        textViewDetailStartTime.setText(startTime);
        textViewDetailFinishTime.setText(finishTime);
        textViewDetailPayment.setText(payment);

        final CollectionReference postsOffersCollectionReference = firebaseFirestore.collection("Posts_Offer");

        postsOffersCollectionReference.whereEqualTo("post_id",postId).whereEqualTo("user_id",userId).whereEqualTo("offer_status","pending").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot snapshots = task.getResult();
                    for (DocumentSnapshot snapshot : snapshots.getDocuments() ){
                        postOfferId = snapshot.getId();

                        final Map<String,Object> data = snapshot.getData();

                        oldOffer = (String) data.get("payment");

                        editTextDetailOffer.getEditText().setText(oldOffer);
                        buttonDetailOffer.setVisibility(View.INVISIBLE);
                        buttonDetailAccept.setVisibility(View.INVISIBLE);
                        buttonDetailOfferUpdate.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        DocumentReference workPostsDocumentReference = firebaseFirestore.collection("WorkPosts").document(postId);

        workPostsDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    final DocumentSnapshot document = task.getResult();

                    if (document.exists()){

                        final Map<String,Object> data = document.getData();
                        final String postUserId = (String) data.get("user_id");
                        final String oldPayment = (String) data.get("payment");

                        if (postUserId.equals(userId)){
                            editTextDetailOffer.getEditText().setText(oldPayment);
                            buttonDetailOffer.setVisibility(View.INVISIBLE);
                            buttonDetailAccept.setVisibility(View.INVISIBLE);
                            buttonDetailUpdatePayment.setVisibility(View.VISIBLE);
                            editTextDetailOffer.setHint("New Price");
                        }

                    }
                }
            }
        });

    }


    public void goToDetailMap (View view){
        // Go To map and mark Post's work
        final Intent postLocationIntent = new Intent(PostDetayActivity.this,PostLocationActivity.class);
        postLocationIntent.putExtra("latitude",latitude);
        postLocationIntent.putExtra("longitude",longitude);
        postLocationIntent.putExtra("title",title);
        postLocationIntent.putExtra("explanation",explanation);
        startActivity(postLocationIntent);
    }

    public void goToAcceptDetailPayment (View view){
        // Accept payment
        //Userid , postid, payment
        writePostOfferToDatabase(payment);
    }

    public void goToOfferDetailPrice (View view){
        // Offer Price
        final String offer = editTextDetailOffer.getEditText().getText().toString();
        if (offer != null) {
            writePostOfferToDatabase(offer);
        }
    }

    public void goToOfferUpdatePayment (View view){

        // update price yapılacak.
        final String newPayment = editTextDetailOffer.getEditText().getText().toString();

        final DocumentReference workPostsDocumentReference = firebaseFirestore.collection("WorkPosts").document(postId);

        workPostsDocumentReference.update("payment",newPayment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent postIntent = new Intent(PostDetayActivity.this,MyPostsActivity.class);
                finish();
                startActivity(postIntent);
            }
        });

    }


    public void goToOfferUpdatePrice(View view){
        final String newOffer = editTextDetailOffer.getEditText().getText().toString();

        final DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(postOfferId);

        postsOfferDocumentReference.update("payment",newOffer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent postIntent = new Intent(PostDetayActivity.this,MyOffersActivity.class);
                finish();
                startActivity(postIntent);
            }
        });

    }



    public void writePostOfferToDatabase(String price){

        HashMap<String, Object> postsOffer = new HashMap<>();

        postsOffer.put("user_id",userId);
        postsOffer.put("post_id",postId);
        postsOffer.put("payment", price);
        postsOffer.put("offer_status","pending");
        postsOffer.put("work_done","not_done");
        postsOffer.put("giving_rating","not_rated");
        postsOffer.put("create_date", FieldValue.serverTimestamp());

        firebaseFirestore.collection("Posts_Offer").add(postsOffer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent postIntent = new Intent(PostDetayActivity.this,MyOffersActivity.class);
                postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(postIntent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostDetayActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }




}