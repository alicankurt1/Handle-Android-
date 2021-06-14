package com.alican.graduationproject1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.se.omapi.Session;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    public static final String RESIM_YOK = "https://firebasestorage.googleapis.com/v0/b/graduationpoject1.appspot.com/o/images%2F13416d49-79b5-46d7-9edd-299e19a7b5e7?alt=media&token=d72dbdbb-311d-4056-bdfb-efbe06fa3c81";
    public static final String IMAGES_PATH = "images/";

    private ImageView imagePost;
    private CheckBox checkBoxPayment;
    private Button buttonDate, buttonStartTime, buttonFinishTime;
    private TextInputLayout editTextAddress, editTextExplanation, editTextTitle, editTextPayment;
    private Spinner spinnerCategories;
    private String address, latitude, longitude, startTime, finishTime, date, category, milisecDate;
    private Long selectionDataForMiliSec;
    private Uri imageUri;
    private Bitmap selectedImage;
    private String downloadUrl;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        getSupportActionBar().setTitle("New Task");

        final Intent valuesIntent = getIntent();
        address = valuesIntent.getStringExtra("address");
        latitude = valuesIntent.getStringExtra("latitude");
        longitude = valuesIntent.getStringExtra("longitude");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        imagePost = findViewById(R.id.imagePost);
        checkBoxPayment = findViewById(R.id.checkBoxPayment);
        buttonDate = findViewById(R.id.buttonDate);
        buttonStartTime = findViewById(R.id.buttonStartTime);
        buttonFinishTime = findViewById(R.id.buttonFinishTime);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextExplanation = findViewById(R.id.editTextExplanation);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPayment = findViewById(R.id.editTextPayment);

        spinnerCategories = findViewById(R.id.spinnerCategoriesUpload);

        editTextAddress.getEditText().setText(address);

        // SPINNERR
        final String [] categories = {"Pet","Carrying","Shopping","Catering","Painting","Cleaning"};

        final ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,categories);
        spinnerCategories.setAdapter(adapterCategories);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categories[position];
                // değişkene ata oradan databaseye eklenecek
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // buraya bakılacak
            }
        });

        // DATE + TIME

            // Calendar
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        final Long today = MaterialDatePicker.todayInUtcMilliseconds();
        calendar.setTimeInMillis(today);

            // CalendarConstraints
        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder();
        calendarConstraintsBuilder.setValidator(DateValidatorPointForward.now());
        //

        final MaterialDatePicker.Builder dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTitleText("Select Work Date");
        dateBuilder.setSelection(today);
        dateBuilder.setCalendarConstraints(calendarConstraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = dateBuilder.build();

        // NEW TRY DATE PICKER
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
               // calendar.setTimeInMillis(Long.parseLong(selection.toString()));
                System.out.println("Selection : " + selection );
                buttonDate.setText(materialDatePicker.getHeaderText());
                date = materialDatePicker.getHeaderText();
                milisecDate = selection.toString();
                selectionDataForMiliSec = Long.parseLong(selection.toString());
            }
        });

        // NEW TRY TIME PICKER
        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog("start");
            }
        });

        buttonFinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog("finish");
            }
        });

        checkBoxPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxPayment.isChecked()){
                    editTextPayment.setVisibility(View.INVISIBLE);
                    editTextPayment.getEditText().setText("0");
                }
                else{
                    editTextPayment.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // SELECT IMAGE
    public void selectImage(View view){
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
                    imagePost.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                    imagePost.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TIME PICKER
    private void showTimePickerDialog(String whichButton){
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourStr,minuteStr;
                if (minute < 10){
                    minuteStr = "0" + minute;
                }else{
                    minuteStr = String.valueOf(minute);
                }
                if (hourOfDay < 10){
                    hourStr = "0" + hourOfDay;
                }else{
                    hourStr = String.valueOf(hourOfDay);
                }

                String time = hourStr + ":" + minuteStr;
                if (whichButton == "start"){
                    buttonStartTime.setText(time);
                    startTime = time;
                }else{
                    buttonFinishTime.setText(time);
                    finishTime = time;
                }

            }
        }, hour, minute, true);
        timePickerDialog.show();
    }


    public void writePostToDatabase(String imageUrl){

        final String title = editTextTitle.getEditText().getText().toString();
        final String explanation = editTextExplanation.getEditText().getText().toString();
        final String payment = editTextPayment.getEditText().getText().toString();
        String paymentSTR = payment;
        if (payment.equals("0")){
            paymentSTR = "(Free) 0";
        }
        final Integer payment_int =  Integer.parseInt(editTextPayment.getEditText().getText().toString());
        final String userUid = firebaseAuth.getUid();
        long milisec_date = Long.parseLong(milisecDate);
        milisec_date = -milisec_date;
        final HashMap<String, Object> postPost = new HashMap<>();

        postPost.put("user_id",userUid);
        postPost.put("image_url",imageUrl);
        postPost.put("category", category);
        postPost.put("title", title);
        postPost.put("explanation", explanation);
        postPost.put("date", date);
        postPost.put("milisec_date",milisec_date);
        postPost.put("start_time", startTime);
        postPost.put("finish_time", finishTime);
        postPost.put("address", address);
        postPost.put("latitude", latitude);
        postPost.put("longitude", longitude);
        postPost.put("payment", paymentSTR);
        postPost.put("payment_int", payment_int);
        postPost.put("offer_id","no_offer");
        postPost.put("post_expire","no_expired");
        postPost.put("create_date", FieldValue.serverTimestamp());

        firebaseFirestore.collection("WorkPosts").add(postPost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // başarılı işlem sonucu
                final Intent loginIntent = new Intent(UploadActivity.this,PostActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // başarısız işlem sonucu
                Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveButton(View view) {
        // imagepost alınacak

        String dateNew = buttonDate.getText().toString();
        String startTimeNew = buttonStartTime.getText().toString();
        String finishTimeNew = buttonFinishTime.getText().toString();




        String title = editTextTitle.getEditText().getText().toString();
        String explanation = editTextExplanation.getEditText().getText().toString();
        String payment = editTextPayment.getEditText().getText().toString();
        String address = editTextAddress.getEditText().getText().toString();

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);
        int startsTimeInt =0,finishTimeInt=0;
        String hourStr,minuteStr;
        if (minute < 10){
            minuteStr = "0" + minute;
        }else{
            minuteStr = String.valueOf(minute);
        }
        if (hour < 10){
            hourStr = "0" + hour;
        }else{
            hourStr = String.valueOf(hour);
        }

        int todayTime = Integer.valueOf(hourStr + minuteStr);

        if (!startTimeNew.equals("Starts at")){
            String startsAt = startTimeNew;
            String[] parts = startsAt.split(":");
            startsTimeInt = Integer.valueOf(parts[0] + parts[1]);
        }
        if (!finishTimeNew.equals("Ends At")){
            String finishAt = finishTimeNew;
            String[] parts = finishAt.split(":");
            finishTimeInt = Integer.valueOf(parts[0] + parts[1]);
        }


        final Long today = MaterialDatePicker.todayInUtcMilliseconds();
        System.out.println("Today : " + today + " - " + selectionDataForMiliSec);



        if (dateNew.equals("Choose a Date")) {
            Snackbar.make(view, "Date cannot be empty!", Snackbar.LENGTH_SHORT).show();
            buttonDate.requestFocus();
            return;
        }
        else if (startTimeNew.equals("Starts at")) {
            Snackbar.make(view, "Start time cannot be empty!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else if (finishTimeNew.equals("Ends At")) {
            Snackbar.make(view, "End time cannot be empty!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else if (address.equals("")) {
            Snackbar.make(view, "Address cannot be empty!", Snackbar.LENGTH_SHORT).show();
            editTextAddress.getEditText().requestFocus();
            return;
        }
        else if (title.equals("")) {
            Snackbar.make(view, "Caption cannot be empty!", Snackbar.LENGTH_SHORT).show();
            editTextTitle.getEditText().requestFocus();
            return;
        }
        else if (explanation.equals("")) {
            Snackbar.make(view, "Description cannot be empty!", Snackbar.LENGTH_SHORT).show();
            editTextExplanation.getEditText().requestFocus();
            return;
        }
        else if (payment.equals("")) {
            Snackbar.make(view, "Price cannot be empty!", Snackbar.LENGTH_SHORT).show();
            editTextPayment.getEditText().requestFocus();
            return;
        }else if (  (today.equals(selectionDataForMiliSec))  && (startsTimeInt < todayTime)){
            Snackbar.make(view, "Start time cannot be earlier than now!", Snackbar.LENGTH_SHORT).show();
            return;
        }else if (finishTimeInt < startsTimeInt){
            Snackbar.make(view, "Finish time cannot be earlier than start time!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Universal Uniqe Id oluşturma
        final UUID uuid = UUID.randomUUID();
        final String imageName = IMAGES_PATH + uuid;

        if (imageUri != null){
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Download URL almaca

                    final StorageReference urlReference = FirebaseStorage.getInstance().getReference(imageName);
                    urlReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();
                            writePostToDatabase(downloadUrl);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            downloadUrl = RESIM_YOK;
                            writePostToDatabase(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }else{
            downloadUrl = RESIM_YOK;
            writePostToDatabase(downloadUrl);
        }
    }
}
