package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    public static final String AVATAR_IMAGES_PATH = "Avatar_Images/";
    public static final String NO_AVATAR = "avatar_yok";

    private TextInputLayout nameText;
    private TextInputLayout surnameText;
    private TextInputLayout emailText;
    private TextInputLayout passwordText;
    private TextInputLayout phoneText;
    private TextInputLayout birthdayText;
    private ImageView imageAvatar;

    private Uri imageUri;
    private Bitmap selectedImage;
    private String downloadUrl;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign Up in Handle");

        imageAvatar = findViewById(R.id.avatarImageView);

        nameText = findViewById(R.id.nameText);
        surnameText = findViewById(R.id.surnameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        phoneText = findViewById(R.id.phoneText);
        birthdayText = findViewById(R.id.birthdayText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void selectAvatar(View view) {
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
                    imageAvatar.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                    imageAvatar.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void writeUserToDatabase(String imageUrl){

        String name = nameText.getEditText().getText().toString();
        String surname = surnameText.getEditText().getText().toString();
        String email = emailText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();
        String phoneNumber = phoneText.getEditText().getText().toString();
        String birthday = birthdayText.getEditText().getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                final String uid = authResult.getUser().getUid();

                final HashMap<String, Object> postUser = new HashMap<>();

                postUser.put("userId", uid);
                postUser.put("image_url",imageUrl);
                postUser.put("name",name);
                postUser.put("surname",surname);
                postUser.put("phone_number",phoneNumber);
                postUser.put("birthday",birthday);
                postUser.put("rating","0");
                postUser.put("rating_count","0");
                postUser.put("date", FieldValue.serverTimestamp());

                firebaseFirestore.collection("UserInformations").add(postUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        firebaseAuth.signOut();
                        Intent loginIntent = new Intent(SignUpActivity.this,MainActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void singUpButton(View view){

        final String name = nameText.getEditText().getText().toString();
        final String surname = surnameText.getEditText().getText().toString();
        final String email = emailText.getEditText().getText().toString();
        final String password = passwordText.getEditText().getText().toString();
        final String phoneNumber = phoneText.getEditText().getText().toString();
        final String birthday = birthdayText.getEditText().getText().toString();

        if (name.equals("")) {
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            nameText.getEditText().requestFocus();
            return;
        }
        else if (surname.equals("")) {
            Toast.makeText(this, "Surname cannot be empty!", Toast.LENGTH_SHORT).show();
            surnameText.getEditText().requestFocus();
            return;
        }
        else if (email.equals("")) {
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
            emailText.getEditText().requestFocus();
            return;
        }
        else if (password.equals("")) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            passwordText.getEditText().requestFocus();
            return;
        }
        else if (phoneNumber.equals("")) {
            Toast.makeText(this, "Phone cannot be empty!", Toast.LENGTH_SHORT).show();
            phoneText.getEditText().requestFocus();
            return;
        }
        else if (birthday.equals("")) {
            Toast.makeText(this, "Birthday cannot be empty!", Toast.LENGTH_SHORT).show();
            birthdayText.getEditText().requestFocus();
            return;
        }

        final UUID uuid2 = UUID.randomUUID();
        final String imageName2 = AVATAR_IMAGES_PATH + uuid2;

        if (imageUri != null){
            storageReference.child(imageName2).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final StorageReference urlReference = FirebaseStorage.getInstance().getReference(imageName2);
                    urlReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            writeUserToDatabase(downloadUrl);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            downloadUrl = NO_AVATAR;
                            writeUserToDatabase(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            downloadUrl = NO_AVATAR;
            writeUserToDatabase(downloadUrl);
        }


    }
}