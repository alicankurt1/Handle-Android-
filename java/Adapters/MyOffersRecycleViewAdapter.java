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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyOffersRecycleViewAdapter extends RecyclerView.Adapter<MyOffersRecycleViewAdapter.MyOffersHolder> {

    private ArrayList<String> postIdList;
    private ArrayList<String> offerIdList;
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
    private ArrayList<String> offerStatusList;
    private ArrayList<String> workDoneList;




    public MyOffersRecycleViewAdapter(ArrayList<String> postIdList, ArrayList<String> offerIdList, ArrayList<String> workDoneList ,ArrayList<String> imageUrlList, ArrayList<String> titleList, ArrayList<String> categoryList, ArrayList<String> explanationList, ArrayList<String> workDateList, ArrayList<String> startTimeList, ArrayList<String> finishTimeList, ArrayList<String> paymentList, ArrayList<String> addressList, ArrayList<String> latitudeList, ArrayList<String> longitudeList, ArrayList<String> offerStatusList ) {
        this.postIdList = postIdList;
        this.offerIdList = offerIdList;
        this.imageUrlList = imageUrlList;
        this.titleList = titleList;
        this.categoryList = categoryList;
        this.explanationList = explanationList;
        this.workDateList = workDateList;
        this.startTimeList = startTimeList;
        this.finishTimeList = finishTimeList;
        this.paymentList = paymentList;
        this.addressList = addressList;
        this.latitudeList = latitudeList;
        this.longitudeList = longitudeList;
        this.offerStatusList = offerStatusList;
        this.workDoneList = workDoneList;

    }

    @NonNull
    @Override
    public MyOffersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_my_offers,parent,false);

        return new MyOffersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOffersHolder holder, int position) {

        //  String postId,imageUrl,title,category,explanation,workDate,startTime,finishTime,address,latitude,longitude,payment;

        holder.postId = postIdList.get(position);
        holder.offerId = offerIdList.get(position);
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


        holder.myOffersTitle.setText(titleList.get(position));
        holder.myOffersCategory.setText(categoryList.get(position));
        holder.myOffersAddress.setText(addressList.get(position));
        holder.myOffersDate.setText(workDateList.get(position));
        holder.myOffersStartTime.setText(startTimeList.get(position));
        holder.myOffersFinishTime.setText(finishTimeList.get(position));
        holder.myOffersOffer.setText(paymentList.get(position));
        Picasso.get().load(imageUrlList.get(position)).into(holder.postImageView);

        if (offerStatusList.get(position).equals("declined")){
            holder.myOffersStatus.setText("Offer is declined.");
            holder.buttonUpdateOffer.setVisibility(View.INVISIBLE);
            holder.buttonCancelOffer.setVisibility(View.INVISIBLE);
            holder.myOffersStatus.setBackgroundResource(R.color.redddd);
        }else if (offerStatusList.get(position).equals("accepted")){

            if (workDoneList.get(position).equals("done")){
                holder.myOffersStatus.setText("Task is done.");
                holder.buttonUpdateOffer.setVisibility(View.INVISIBLE);
                holder.buttonCancelOffer.setVisibility(View.INVISIBLE);
                holder.buttonWorkDone.setVisibility(View.INVISIBLE);
                holder.myOffersStatus.setBackgroundResource(R.color.dark_green);
            }else{
                holder.myOffersStatus.setText("Offer is accepted.");
                holder.buttonUpdateOffer.setVisibility(View.INVISIBLE);
                holder.buttonCancelOffer.setVisibility(View.INVISIBLE);
                holder.buttonWorkDone.setVisibility(View.VISIBLE);
                holder.myOffersStatus.setBackgroundResource(R.color.light_green);
            }

        }else if (offerStatusList.get(position).equals("canceled")){
            holder.myOffersStatus.setText("Offer is cancelled.");
            holder.buttonUpdateOffer.setVisibility(View.INVISIBLE);
            holder.buttonCancelOffer.setVisibility(View.INVISIBLE);
            holder.myOffersStatus.setBackgroundResource(R.color.alter_2);
        }else{
            holder.myOffersStatus.setText("Offer is pending.");
        }

        // offerstatusler alınıp ona göre gösterilecek

    }

    @Override
    public int getItemCount() { return postIdList.size(); }


    class MyOffersHolder extends RecyclerView.ViewHolder{

        ImageView postImageView;
        TextView myOffersTitle,myOffersAddress,myOffersDate,myOffersStartTime,myOffersFinishTime,myOffersOffer,myOffersStatus,myOffersCategory;
        Button buttonCancelOffer,buttonUpdateOffer,buttonWorkDone;

        String postId,offerId,imageUrl,title,category,explanation,workDate,startTime,finishTime,address,latitude,longitude,payment;
        FirebaseFirestore firebaseFirestore;



        public MyOffersHolder(@NonNull View itemView) {
            super(itemView);
            firebaseFirestore = FirebaseFirestore.getInstance();

            postImageView = itemView.findViewById(R.id.imageViewRecyclePostsOffersAvatar2);
            myOffersTitle = itemView.findViewById(R.id.textViewRecyclePostsOffersName2);
            myOffersCategory = itemView.findViewById(R.id.textViewRecycleMyOffersCategory);
            myOffersAddress = itemView.findViewById(R.id.textViewRecycleMyOffersAddress2);
            myOffersDate = itemView.findViewById(R.id.textViewRecyclePostsOffersBirthday2);
            myOffersStartTime = itemView.findViewById(R.id.textViewRecyclePostsOffersPhoneNumber2);
            myOffersFinishTime = itemView.findViewById(R.id.textViewRecycleMyOffersFinishTime2);
            myOffersOffer = itemView.findViewById(R.id.textViewRecyclePostsOffersOffer2);
            myOffersStatus = itemView.findViewById(R.id.textViewRecycleMyOffersOfferStatus2);



            buttonUpdateOffer = itemView.findViewById(R.id.buttonRecycleMyOffersUpdateOffer2);
            buttonCancelOffer = itemView.findViewById(R.id.buttonRecyclePostsOffersDecline2);
            buttonWorkDone = itemView.findViewById(R.id.buttonRecycleMyOffersWorkDone2);

            // button clickler yazılacak button.setOnclickListener..


            // Intent detay activity
            buttonUpdateOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetayIntent = new Intent(itemView.getContext(), PostDetayActivity.class);
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

            // Cancel our offer
            buttonCancelOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);

                    postsOfferDocumentReference.update("offer_status","canceled").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println(" offer_status başarıyla update edildi!");
                            ((Activity)itemView.getContext()).finish();
                            itemView.getContext().startActivity(new Intent(itemView.getContext(),MyOffersActivity.class) );

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(" offer_status update başarısız oldu!");
                        }
                    });
                }
            });

            // I done work
            buttonWorkDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);

                    postsOfferDocumentReference.update("work_done","done").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println(" work_done başarıyla update edildi!");
                            ((Activity)itemView.getContext()).finish();
                            itemView.getContext().startActivity(new Intent(itemView.getContext(),MyOffersActivity.class) );
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println(" work_done update başarısız oldu!");
                        }
                    });
                }
            });


        }


    }



}
