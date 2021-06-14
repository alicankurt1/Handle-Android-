package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout emailText;
    private TextInputLayout passwordText;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = findViewById(R.id.loginEmailText);
        passwordText = findViewById(R.id.loginPasswordText);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null ){
            Intent postIntent = new Intent(MainActivity.this,PostActivity.class);
            startActivity(postIntent);
            finish();
        }

    }

    public void signInButton(View view){
        String email = emailText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();

        if (email.equals("")) {
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
            emailText.getEditText().requestFocus();
            return;
        }
        else if (password.equals("")) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            passwordText.getEditText().requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent postIntent = new Intent(MainActivity.this,PostActivity.class);
                startActivity(postIntent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void goSingUpButton(View view){
        Intent singUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(singUpIntent);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}