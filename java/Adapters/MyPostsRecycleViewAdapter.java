package com.alican.graduationproject1;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyPostsRecycleViewAdapter extends RecyclerView.Adapter<MyPostsRecycleViewAdapter.MyPostsHolder> {

    private ArrayList<String> postIdList;
    private ArrayList<String> imageUrlList;
    private ArrayList<String> titleList;
    private ArrayList<String> categoryList;
    private ArrayList<String> explanationList;
    private ArrayList<String> workDateList;
    private ArrayList<String> startTimeList;
    private ArrayList<String> finishTimeList;
    private ArrayList<String> paymentList;
    private ArrayList<String> addressList;
    private ArrayList<String> latitudeList;
    private ArrayList<String> longitudeList;

    private OnMyPostsListener mOnMyPostsListener;


    public MyPostsRecycleViewAdapter(   ArrayList<String> explanationList,ArrayList<String> addressList,ArrayList<String> latitudeList, ArrayList<String> longitudeList,     ArrayList<String> postIdList, ArrayList<String> imageUrlList, ArrayList<String> titleList, ArrayList<String> categoryList, ArrayList<String> workDateList, ArrayList<String> startTimeList, ArrayList<String> finishTimeList, ArrayList<String> paymentList, OnMyPostsListener onMyPostsListener) {
        this.postIdList = postIdList;
        this.imageUrlList = imageUrlList;
        this.titleList = titleList;
        this.categoryList = categoryList;
        this.workDateList = workDateList;
        this.startTimeList = startTimeList;
        this.finishTimeList = finishTimeList;
        this.paymentList = paymentList;
        this.latitudeList = latitudeList;
        this.longitudeList = longitudeList;
        this.explanationList = explanationList;
        this.addressList = addressList;



        this.mOnMyPostsListener = onMyPostsListener;
    }

    @NonNull
    @Override
    public MyPostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_my_posts, parent,false);

        return new MyPostsHolder(view, mOnMyPostsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsHolder holder, int position) {
        //holder.myPostsTextViewTitle.setText();
        holder.postId = postIdList.get(position);
        holder.imageUrl = imageUrlList.get(position);
        holder.title = titleList.get(position);
        holder.category = categoryList.get(position);
        holder.explanation = explanationList.get(position);
        holder.workDate = workDateList.get(position);
        holder.startTime = startTimeList.get(position);
        holder.finishTime = finishTimeList.get(position);
        holder.address = addressList.get(position);
        holder.latitude = latitudeList.get(position);
        holder.longitude = longitudeList.get(position);
        holder.payment = paymentList.get(position);

        holder.myPostsTextViewTitle.setText(titleList.get(position));
        holder.myPostsTextViewCategory.setText(categoryList.get(position));
        holder.myPostsTextViewDate.setText(workDateList.get(position));
        holder.myPostsTextViewStartTime.setText(startTimeList.get(position));
        holder.myPostsTextViewFinishTime.setText(finishTimeList.get(position));
        holder.myPostsTextViewPayment.setText(paymentList.get(position));
        // ücret gösterilecek
        Picasso.get().load(imageUrlList.get(position)).into(holder.myPostsImageView);



    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    static class MyPostsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String postId,imageUrl,title,category,explanation,workDate,startTime,finishTime,address,latitude,longitude,payment;
        private final ImageView myPostsImageView;
        private final TextView myPostsTextViewTitle;
        private final TextView myPostsTextViewDate;
        private final TextView myPostsTextViewStartTime;
        private final TextView myPostsTextViewFinishTime;
        private final TextView myPostsTextViewPayment;
        private final TextView myPostsTextViewCategory;
        private Button myPostsDeleteButton, myPostsUpdateButton;
        private final OnMyPostsListener onMyPostsListener;

        private final FirebaseFirestore firebaseFirestore;


        public MyPostsHolder(@NonNull View itemView, OnMyPostsListener onMyPostsListener) {
            super(itemView);

            firebaseFirestore = FirebaseFirestore.getInstance();

            myPostsImageView = itemView.findViewById(R.id.imageViewRecycleMyPostsImage);
            myPostsTextViewTitle = itemView.findViewById(R.id.textViewRecycleMyPostsTitle);
            myPostsTextViewCategory = itemView.findViewById(R.id.textViewRecycleMyPostsCategory);
            myPostsTextViewDate = itemView.findViewById(R.id.textViewRecycleMyPostsDate);
            myPostsTextViewStartTime = itemView.findViewById(R.id.textViewRecycleMyPostsStartTime);
            myPostsTextViewFinishTime = itemView.findViewById(R.id.textViewRecycleMyPostsFinishTime);
            myPostsTextViewPayment = itemView.findViewById(R.id.textViewRecycleMyPostsPayment);
            myPostsDeleteButton = itemView.findViewById(R.id.buttonRecycleMyPostsDelete);
            myPostsUpdateButton = itemView.findViewById(R.id.buttonRecycleMyPostsUpdate);

            // Intent detay activity
            myPostsUpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent postDetayIntent = new Intent(itemView.getContext(), PostDetayActivity.class);
                    postDetayIntent.putExtra("post_id",postId);
                    postDetayIntent.putExtra("image_url",imageUrl);
                    postDetayIntent.putExtra("title",title);
                    postDetayIntent.putExtra("category",category);
                    postDetayIntent.putExtra("explanation",explanation);
                    postDetayIntent.putExtra("work_date",workDate);
                    postDetayIntent.putExtra("start_time",startTime);
                    postDetayIntent.putExtra("finish_time",finishTime);
                    postDetayIntent.putExtra("address",address);
                    postDetayIntent.putExtra("latitude",latitude);
                    postDetayIntent.putExtra("longitude",longitude);
                    postDetayIntent.putExtra("payment",payment);
                    itemView.getContext().startActivity(postDetayIntent);
                }
            });



            myPostsDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // silme işlemleri yapılacak
                    final DocumentReference workPostsDocumentReference = firebaseFirestore.collection("WorkPosts").document(postId);

                    workPostsDocumentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // silme işlemi başarılı fakat posts-offer tablosundan bu işe ait tüm teklifler silinecek.
                            final CollectionReference postsOfferCollectionReference = firebaseFirestore.collection("Posts_Offer");

                            postsOfferCollectionReference.whereEqualTo("post_id",postId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){

                                        final QuerySnapshot snapshots = task.getResult();
                                        for (DocumentSnapshot snapshot : snapshots.getDocuments() ){

                                            final String offerId = snapshot.getId();
                                            final DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);
                                            postsOfferDocumentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    System.out.println("Offer Başarıyla silindi ..");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("Offer silme BAŞARISIZ OLDU ..");
                                                }
                                            });

                                        }
                                    }
                                }
                            });

                        }
                    });

                    ((Activity)itemView.getContext()).finish();
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),MyPostsActivity.class) );
                }
            });

            this.onMyPostsListener = onMyPostsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMyPostsListener.onMyPostsClick(getAdapterPosition());
        }
    }

    public interface OnMyPostsListener{
        void onMyPostsClick(int position);
    }



}
