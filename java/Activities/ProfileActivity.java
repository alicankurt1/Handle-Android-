package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    public static final String NO_AVATAR = "avatar_yok";
    public static final String AVATAR_IMAGES_PATH = "Avatar_Images/";

    private String userId, userEmail, avatarUrl, userName, userSurname, userPhoneNumber, userBirthday, userRating, userInfoId;
    private ImageView imageViewAvatar;
    private TextView textViewProfileRating, textViewProfileEmail, textViewProfileName, textViewProfileSurname, textViewProfilePhoneNumber, textViewProfileBirthday, textViewProfileChangeEmail, textViewProfileChangePassword, textViewProfileChangeDetails;
    private TextInputLayout tilProfileEmail, tilProfilePasswordForMail, tilProfileName, tilProfileSurname, tilProfilePhone, tilProfileBirthday, tilProfileOldPassword, tilProfileNewPassword;
    private Button buttonChangeEmail, buttonChangePassword, buttonChangeDetails, buttonChangeAvatar;
    private ConstraintLayout layoutProfileInfo, layoutProfileEmail, layoutProfilePassword;

    private Uri imageUri;
    private Bitmap selectedImage;
    private String downloadUrl;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        final BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewProfile);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        Intent homeIntent = new Intent(ProfileActivity.this,PostActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.add_post:
                        Intent selectLocationIntent = new Intent(ProfileActivity.this,SelectLocationActivity.class);
                        startActivity(selectLocationIntent);
                        break;
                    case R.id.profile:
                        finish();
                        Intent profileIntent = new Intent(ProfileActivity.this, MySettingsActivity.class);
                        startActivity(profileIntent);
                        break;
                }

                return true;
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            userId = user.getUid();
            userEmail = user.getEmail();
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        imageViewAvatar = findViewById(R.id.imageViewProfile);
        textViewProfileRating = findViewById(R.id.txtProfileRating);
        textViewProfileEmail = findViewById(R.id.txtProfileEmail);
        //textViewProfilePassword = findViewById(R.id.textViewProfilePassword);
        textViewProfileName = findViewById(R.id.txtProfileName);
        textViewProfileSurname = findViewById(R.id.txtProfileSurname);
        textViewProfilePhoneNumber = findViewById(R.id.txtProfilePhone);
        textViewProfileBirthday = findViewById(R.id.txtProfileBirthday);
        // text click
        textViewProfileChangeEmail = findViewById(R.id.txtProfileChangeEmail);
        textViewProfileChangePassword = findViewById(R.id.txtProfileChangePassword);
        textViewProfileChangeDetails = findViewById(R.id.txtChangePersonalData);

        // layout
        layoutProfileInfo = findViewById(R.id.layoutProfileInfo);
        layoutProfileEmail = findViewById(R.id.layoutProfileEmail);
        layoutProfilePassword = findViewById(R.id.layoutProfilePassword);

        //Buttons
        buttonChangeEmail = findViewById(R.id.buttonProfileChangeEmail);
        buttonChangePassword = findViewById(R.id.buttonProfileChangePassword);
        buttonChangeDetails = findViewById(R.id.buttonProfileChangeDetails);
        buttonChangeAvatar = findViewById(R.id.buttonProfileChangeAvatar);

        //EditTexts
        tilProfileEmail = findViewById(R.id.tilProfileEmail);
        tilProfilePasswordForMail = findViewById(R.id.tilProfilePasswordForMail);
        tilProfileName = findViewById(R.id.tilProfileName);
        tilProfileSurname = findViewById(R.id.tilProfileSurname);
        tilProfilePhone = findViewById(R.id.tilProfilePhone);
        tilProfileBirthday = findViewById(R.id.tilProfileBirthday);
        tilProfileOldPassword = findViewById(R.id.tilProfileOldPassword);
        tilProfileNewPassword = findViewById(R.id.tilProfileNewPassword);

        // Implements

        getUserInformationsFromDB();

    }

    public void onClickChangePersonalData(View view) {
        layoutProfileInfo.setVisibility(View.VISIBLE);
    }

    public void onClickedProfileCancelInfo(View view) {
        layoutProfileInfo.setVisibility(View.INVISIBLE);
    }

    public void onClickProfileCancelEmail(View view) {
        layoutProfileEmail.setVisibility(View.INVISIBLE);
    }

    public void onClickProfileCancelNewPassword(View view) {
        layoutProfilePassword.setVisibility(View.INVISIBLE);
    }

    public void changeAvatarClicked(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 2);
        }

    }


    public void getUserInformationsFromDB(){
        // User bilgilerini alma.
        final CollectionReference userInfoCollectionReference = firebaseFirestore.collection("UserInformations");
        userInfoCollectionReference.whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){

                        final Map<String,Object> userData = document.getData();
                        avatarUrl = (String) userData.get("image_url");
                        userName = (String) userData.get("name");
                        userSurname = (String) userData.get("surname");
                        userPhoneNumber = (String) userData.get("phone_number");
                        userBirthday = (String) userData.get("birthday");
                        userRating = (String) userData.get("rating");

                        Picasso.get().load(avatarUrl).into(imageViewAvatar);

                        final Float gaveRating = Float.parseFloat(userRating);
                        final DecimalFormat df = new DecimalFormat("#.##");
                        df.setRoundingMode(RoundingMode.CEILING);


                        textViewProfileRating.setText(df.format(gaveRating));
                        textViewProfileEmail.setText(userEmail);
                        textViewProfileName.setText(userName);
                        textViewProfileSurname.setText(userSurname);
                        textViewProfilePhoneNumber.setText(userPhoneNumber);
                        textViewProfileBirthday.setText(userBirthday);

                        tilProfileName.getEditText().setText(userName);
                        tilProfileSurname.getEditText().setText(userSurname);
                        tilProfilePhone.getEditText().setText(userPhoneNumber);
                        tilProfileBirthday.getEditText().setText(userBirthday);
                        //textViewProfilePassword.setText("*******");

                        tilProfileEmail.getEditText().setText(userEmail);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Exception Profile : " + e.getLocalizedMessage());
            }
        });
    }


        // CHANGE EMAIL

    public void reAuthenticate(String pass,String whichProcess){

        final AuthCredential credential = EmailAuthProvider
                .getCredential(userEmail, pass);

        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (whichProcess.equals("email")){
                    changeEmailFromDB();
                }else if (whichProcess.equals("password")){
                    changePasswordFromDB();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Wrong Password",Toast.LENGTH_LONG).show();
            }
        });


    }

    public void changeEmailTextView(View view){
        // textViewProfileEmail.setVisibility(View.INVISIBLE);
        // textViewProfileChangeEmail.setVisibility(View.INVISIBLE);
        // tilProfileEmail.setVisibility(View.VISIBLE);
        // editTextProfilePasswordCheck.setVisibility(View.VISIBLE);
        // buttonChangeEmail.setVisibility(View.VISIBLE);
        layoutProfileEmail.setVisibility(View.VISIBLE);
    }

    public void changeEmailButton(View view){
        final String pass = tilProfilePasswordForMail.getEditText().getText().toString();
        reAuthenticate(pass,"email");
        //layoutProfileEmail.setVisibility(View.INVISIBLE);
    }

    public void changeEmailFromDB(){

        final String email = tilProfileEmail.getEditText().getText().toString();
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(ProfileActivity.this, "Email has been changed.",Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Check your email!",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void changePassword(View view){
        //textViewProfilePassword.setVisibility(View.INVISIBLE);
        // textViewProfileChangePassword.setVisibility(View.INVISIBLE);
        // buttonChangePassword.setVisibility(View.VISIBLE);
        // tilProfilePasswordForMail.setVisibility(View.VISIBLE);
        // editTextProfilePasswordCheck.setVisibility(View.VISIBLE);
        layoutProfilePassword.setVisibility(View.VISIBLE);
    }


    public void changePasswordButton(View view){
        final String pass = tilProfileOldPassword.getEditText().getText().toString();
        reAuthenticate(pass,"password");
    }

    public void changePasswordFromDB(){
        final String newPassword = tilProfileNewPassword.getEditText().getText().toString();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(getIntent());
                            Toast.makeText(ProfileActivity.this, "Password has been changed.",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Please type a valid password.",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changeYourDetails(View view){
        //visible invisible işlemleri
        textViewProfileName.setVisibility(View.INVISIBLE);
        textViewProfileSurname.setVisibility(View.INVISIBLE);
        textViewProfilePhoneNumber.setVisibility(View.INVISIBLE);
        textViewProfileBirthday.setVisibility(View.INVISIBLE);
        textViewProfileChangeDetails.setVisibility(View.INVISIBLE);

        tilProfileName.setVisibility(View.VISIBLE);
        tilProfileSurname.setVisibility(View.VISIBLE);
        tilProfilePhone.setVisibility(View.VISIBLE);
        tilProfileBirthday.setVisibility(View.VISIBLE);
        buttonChangeDetails.setVisibility(View.VISIBLE);
    }

    public void changeDetailsButton(View view){

        final String newName = tilProfileName.getEditText().getText().toString();
        final String newSurname = tilProfileSurname.getEditText().getText().toString();
        final String newPhoneNumber = tilProfilePhone.getEditText().getText().toString();
        final String newBirthday = tilProfileBirthday.getEditText().getText().toString();

        final CollectionReference userInfoCollectionReference = firebaseFirestore.collection("UserInformations");

        userInfoCollectionReference.whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    QuerySnapshot snapshots = task.getResult();
                    for (DocumentSnapshot snapshot : snapshots.getDocuments() ){
                        userInfoId = snapshot.getId();

                        final DocumentReference userInfoDocumentReference = firebaseFirestore.collection("UserInformations").document(userInfoId);

                        userInfoDocumentReference.update("name",newName,"surname",newSurname,"phone_number",newPhoneNumber,"birthday",newBirthday).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                                startActivity(getIntent());
                                Toast.makeText(ProfileActivity.this, "Detaylar başarıyla değiştirildi.",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Detaylar değişimi başarısız oldu..",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }


    public void changeAvatar(View view){

        buttonChangeAvatar.setVisibility(View.VISIBLE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                }
                imageViewAvatar.setImageBitmap(selectedImage);

                changeProfileAvatarButton();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void changeAvatarOnDatabase(String downloadUrl){

        final CollectionReference userInfoCollectionReference = firebaseFirestore.collection("UserInformations");

        userInfoCollectionReference.whereEqualTo("userId",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    QuerySnapshot snapshots = task.getResult();
                    for (DocumentSnapshot snapshot : snapshots.getDocuments() ){
                        userInfoId = snapshot.getId();

                        DocumentReference userInfoDocumentReference = firebaseFirestore.collection("UserInformations").document(userInfoId);

                        userInfoDocumentReference.update("image_url",downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Avatar has been changed successfully.",Toast.LENGTH_LONG).show();
                               // buttonChangeAvatar.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Avatar has not been changed successfully.",Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                }
            }
        });


    }

    public void changeProfileAvatarButton(){

        // Universal Uniqe Id oluşturma
        final UUID uuid2 = UUID.randomUUID();
        final String imageName2 = AVATAR_IMAGES_PATH + uuid2;

        if (imageUri != null){
            storageReference.child(imageName2).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Download URL almaca
                    final StorageReference urlReference = FirebaseStorage.getInstance().getReference(imageName2);
                    urlReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            changeAvatarOnDatabase(downloadUrl);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            downloadUrl = NO_AVATAR;
                            changeAvatarOnDatabase(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ProfileActivity.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }else{
            downloadUrl = NO_AVATAR;
            changeAvatarOnDatabase(downloadUrl);
        }


    }





}