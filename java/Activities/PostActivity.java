package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

public class PostActivity extends AppCompatActivity implements PostRecycleViewAdapter.OnPostListener {

    public static final String SORT_CREATE_DATE = "create_date";
    public static final String SORT_PAYMENT_INT = "payment_int";
    public static final String SORT_MILISEC_DATE = "milisec_date";
    private String userId, sort;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

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

    private Button buttonNavigatorAllPostsInMap;
    private Spinner spinnerSortingPosts;

    private RecyclerView recyclerView;
    private PostRecycleViewAdapter postRecycleViewAdapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo_white);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        final BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewPost);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        startActivity(getIntent());
                        break;
                    case R.id.add_post:
                        Intent selectLocationIntent = new Intent(PostActivity.this,SelectLocationActivity.class);
                        startActivity(selectLocationIntent);
                        break;
                    case R.id.profile:
                        Intent profileIntent = new Intent(PostActivity.this, MySettingsActivity.class);
                        startActivity(profileIntent);
                        break;
                }

                return true;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        sort = SORT_CREATE_DATE;

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            userId = user.getUid();
        }

        spinnerSortingPosts = findViewById(R.id.spinnerSortingPosts);

        String [] sortingList = {"Creating Date","Best Payment","Close Date"};

        final ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,sortingList);
        spinnerSortingPosts.setAdapter(adapterCategories);

        spinnerSortingPosts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final String sortSTR = sortingList[position];

                switch (sortSTR){
                    case "Creating Date":
                        sort = SORT_CREATE_DATE;
                        break;
                    case "Best Payment":
                        sort = SORT_PAYMENT_INT;
                        break;
                    case "Close Date":
                        sort = SORT_MILISEC_DATE;
                        break;
                }

                getDataFromWorkPosts(sort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sort = SORT_CREATE_DATE;
                getDataFromWorkPosts(sort);
            }
        });

        // Navigator Buttons
        buttonNavigatorAllPostsInMap = findViewById(R.id.buttonNavigatorAllPostsInMap);

        buttonNavigatorAllPostsInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatorAllPostsInMap();
            }
        });

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

        // RecycleView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postRecycleViewAdapter = new PostRecycleViewAdapter(imagesFromFB, titleFromFB,categoryFromFB,workDateFromFB,startTimeFromFB,finishTimeFromFB,paymentFromFB,this);
        recyclerView.setAdapter(postRecycleViewAdapter);

    }

    public void getDataFromWorkPosts(String sorting){

        final CollectionReference workPostsReference = firebaseFirestore.collection("WorkPosts");

        // Filtreleme yapmak istersen
        // workPostsReference.whereEqualTo("category","Pet").addSnapshotListener(.......)
        // Crate date ye göre sırala
        // workPostsReference.orderBy("create_date", Query.Direction.DESCENDING).addSnapshotListener

        // Kullanıcı sıralarken sadece "create_date" kısmına string atansın!!

        workPostsReference.whereEqualTo("offer_id","no_offer").whereEqualTo("post_expire","no_expired").orderBy(sorting, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(PostActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value != null){

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

                    for (DocumentSnapshot line : value.getDocuments()){

                        final Map<String, Object> data = line.getData();
                        // Casting
                        String imageUrl = (String) data.get("image_url");
                        String title = (String) data.get("title");
                        String category = (String) data.get("category");
                        String explanation = (String) data.get("explanation");
                        String date = (String) data.get("date");
                        String start_time = (String) data.get("start_time");
                        String finish_time = (String) data.get("finish_time");
                        String address = (String) data.get("address");
                        String latitude = (String) data.get("latitude");
                        String longitude = (String) data.get("longitude");
                        String payment = (String) data.get("payment");
                        // Create Date hata veriyor
                        // String create_date = (String) data.get("create_date");

                        postIdFromDB.add(line.getId());
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

                        postRecycleViewAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    public void navigatorAllPostsInMap() {

        final Intent allPostInMapIntent = new Intent(PostActivity.this,AllPostsInMapActivity.class);
        allPostInMapIntent.putStringArrayListExtra("postIdFromDB",postIdFromDB);
        allPostInMapIntent.putStringArrayListExtra("titleFromFB",titleFromFB);
        allPostInMapIntent.putStringArrayListExtra("imagesFromFB",imagesFromFB);
        allPostInMapIntent.putStringArrayListExtra("categoryFromFB",categoryFromFB);
        allPostInMapIntent.putStringArrayListExtra("explanationFromFB",explanationFromFB);
        allPostInMapIntent.putStringArrayListExtra("workDateFromFB",workDateFromFB);
        allPostInMapIntent.putStringArrayListExtra("startTimeFromFB",startTimeFromFB);
        allPostInMapIntent.putStringArrayListExtra("finishTimeFromFB",finishTimeFromFB);
        allPostInMapIntent.putStringArrayListExtra("addressFromFB",addressFromFB);
        allPostInMapIntent.putStringArrayListExtra("latitudeFromFB",latitudeFromFB);
        allPostInMapIntent.putStringArrayListExtra("longitudeFromFB",longitudeFromFB);
        allPostInMapIntent.putStringArrayListExtra("paymentFromFB",paymentFromFB);
        startActivity(allPostInMapIntent);

    }

    @Override
    public void onPostClick(int position) {

        final String postId = postIdFromDB.get(position);
        final String imageUrl = imagesFromFB.get(position);
        final String title = titleFromFB.get(position);
        final String category = categoryFromFB.get(position);
        final String explanation = explanationFromFB.get(position);
        final String workDate = workDateFromFB.get(position);
        final String startTime = startTimeFromFB.get(position);
        final String finishTime = finishTimeFromFB.get(position);
        final String address = addressFromFB.get(position);
        final String latitude = latitudeFromFB.get(position);
        final String longitude = longitudeFromFB.get(position);
        final String payment = paymentFromFB.get(position);

        final Intent postDetailIntent = new Intent(PostActivity.this, PostDetayActivity.class);
        postDetailIntent.putExtra("post_id", postId);
        postDetailIntent.putExtra("image_url", imageUrl);
        postDetailIntent.putExtra("title", title);
        postDetailIntent.putExtra("category", category);
        postDetailIntent.putExtra("explanation", explanation);
        postDetailIntent.putExtra("work_date", workDate);
        postDetailIntent.putExtra("start_time", startTime);
        postDetailIntent.putExtra("finish_time", finishTime);
        postDetailIntent.putExtra("address", address);
        postDetailIntent.putExtra("latitude", latitude);
        postDetailIntent.putExtra("longitude", longitude);
        postDetailIntent.putExtra("payment", payment);
        startActivity(postDetailIntent);
    }
}