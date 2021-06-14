package com.alican.graduationproject1;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;


public class PostsOffersRecycleViewAdapter extends RecyclerView.Adapter<PostsOffersRecycleViewAdapter.PostsOffersHolder> {

    private final ArrayList<String> userInfoIdList;
    private final ArrayList<String> offerIdList;
    private final ArrayList<String> avatarUrlList;
    private final ArrayList<String> userNameList;
    private final ArrayList<String> userSurnameList;
    private final ArrayList<String> userPhoneNumberList;
    private final ArrayList<String> userBirthdayList;
    private final ArrayList<String> userRatingList;
    private final ArrayList<String> usersOfferList;
    private final ArrayList<String> offerStatusList;
    private final ArrayList<String> workDoneList;
    private final ArrayList<String> givingRatingList;
    private final String postId;
    private final String postTitle;

    //private FirebaseFirestore firebaseFirestore;

    public PostsOffersRecycleViewAdapter(ArrayList<String> userInfoIdList ,ArrayList<String> workDoneList , ArrayList<String> givingRatingList   ,ArrayList<String> avatarUrlList, ArrayList<String> userNameList, ArrayList<String> userSurnameList, ArrayList<String> userPhoneNumberList, ArrayList<String> userBirthdayList, ArrayList<String> userRatingList, ArrayList<String> usersOfferList, ArrayList<String> offerIdList,ArrayList<String> offerStatusList, String postId, String postTitle) {
        this.userInfoIdList = userInfoIdList;
        this.avatarUrlList = avatarUrlList;
        this.userNameList = userNameList;
        this.userSurnameList = userSurnameList;
        this.userPhoneNumberList = userPhoneNumberList;
        this.userBirthdayList = userBirthdayList;
        this.userRatingList = userRatingList;
        this.usersOfferList = usersOfferList;
        this.offerIdList = offerIdList;
        this.offerStatusList = offerStatusList;
        this.postId = postId;
        this.postTitle = postTitle;
        this.workDoneList = workDoneList;
        this.givingRatingList = givingRatingList;
    }


    @NonNull
    @Override
    public PostsOffersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_posts_offers,parent,false);

        return new PostsOffersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsOffersHolder holder, int position) {

        final Float gaveRating = Float.parseFloat(userRatingList.get(position));
        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);


        holder.postsOffersRating.setText(df.format(gaveRating));
        holder.postsOffersName.setText(userNameList.get(position));
        holder.postsOffersSurname.setText(userSurnameList.get(position));
        holder.postsOffersBirthday.setText(userBirthdayList.get(position));
        holder.postsOffersPhoneNumber.setText(userPhoneNumberList.get(position));
        holder.postsOffersOffer.setText(usersOfferList.get(position));
        holder.userInfoId = userInfoIdList.get(position);
        holder.position = position;
        holder.userName = userNameList.get(position);
        holder.offerId = offerIdList.get(position);
        Picasso.get().load(avatarUrlList.get(position)).into(holder.avatarImageView);

        holder.offerStatus = offerStatusList.get(position);

        if ( holder.offerStatus.equals("accepted")) {

            if (workDoneList.get(position).equals("done")) {

                if(givingRatingList.get(position).equals("rated")){
                    holder.postsOffersStatus.setText("Task is done and rated.");
                    holder.buttonAccept.setVisibility(View.INVISIBLE);
                    holder.buttonDecline.setVisibility(View.INVISIBLE);
                    holder.postsOffersStatus.setBackgroundResource(R.color.dark_green);
                    holder.ratingBar.setEnabled(false);
                } else if(givingRatingList.get(position).equals("not_rated")) {
                    holder.postsOffersStatus.setText("Task is done.");
                    holder.buttonAccept.setVisibility(View.INVISIBLE);
                    holder.buttonDecline.setVisibility(View.INVISIBLE);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.postsOffersStatus.setBackgroundResource(R.color.alter_2);
                }
            } else {
                holder.postsOffersStatus.setText("Offer is accepted.");
                holder.buttonAccept.setVisibility(View.INVISIBLE);
                holder.buttonDecline.setVisibility(View.INVISIBLE);
                holder.postsOffersStatus.setBackgroundResource(R.color.light_green);
            }
            // burada accepted yazısı olacak yeşil halde
        } else if( holder.offerStatus.equals("declined")) {
            holder.buttonAccept.setVisibility(View.INVISIBLE);
            holder.buttonDecline.setVisibility(View.INVISIBLE);
            holder.postsOffersStatus.setText("Offer is declined.");
            // burada DECLINED yazısı kırmızı olacak
        }

    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    class PostsOffersHolder extends RecyclerView.ViewHolder{

        private final ImageView avatarImageView;
        private final TextView postsOffersRating;
        private final TextView postsOffersName;
        private final TextView postsOffersSurname;
        private final TextView postsOffersBirthday;
        private final TextView postsOffersPhoneNumber;
        private final TextView postsOffersOffer;
        private final TextView postsOffersStatus;
        private Integer position;
        private String userName,offerId,offerStatus,userInfoId,gaveRating;
        private final Button buttonAccept;
        private final Button buttonDecline;
        private final RatingBar ratingBar;
        private final FirebaseFirestore firebaseFirestore;

        public PostsOffersHolder(@NonNull View itemView) {
            super(itemView);

            avatarImageView = itemView.findViewById(R.id.imageViewRecyclePostsOffersAvatar2);
            postsOffersRating = itemView.findViewById(R.id.textViewRecyclePostsOffersRating2);
            postsOffersName = itemView.findViewById(R.id.textViewRecyclePostsOffersName2);
            postsOffersSurname = itemView.findViewById(R.id.textViewRecycleMyOffersCategory);
            postsOffersBirthday = itemView.findViewById(R.id.textViewRecyclePostsOffersBirthday2);
            postsOffersPhoneNumber = itemView.findViewById(R.id.textViewRecyclePostsOffersPhoneNumber2);
            postsOffersOffer = itemView.findViewById(R.id.textViewRecyclePostsOffersOffer2);
            postsOffersStatus = itemView.findViewById(R.id.textViewRecyclePostsOffersStatus2);

            buttonAccept = itemView.findViewById(R.id.buttonRecyclePostsOffersAccept2);
            buttonDecline = itemView.findViewById(R.id.buttonRecyclePostsOffersDecline2);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            firebaseFirestore = FirebaseFirestore.getInstance();

            // Offer Kabul etme
            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DocumentReference workPostsDocumentReference = firebaseFirestore.collection("WorkPosts").document(postId);
                    final DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);

                    // Workler tablosunda offer_id güncelleme
                    workPostsDocumentReference.update("offer_id",offerId);

                    // Post Offer tablosunda offer_status güncellenecek
                    postsOfferDocumentReference.update("offer_status","accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final CollectionReference postsOfferCollectionReference = firebaseFirestore.collection("Posts_Offer");

                            postsOfferCollectionReference.whereEqualTo("post_id",postId).whereEqualTo("offer_status","pending").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        final QuerySnapshot snapshots = task.getResult();
                                        for (DocumentSnapshot snapshot : snapshots.getDocuments() ){
                                            String offerId = snapshot.getId();

                                            final DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);
                                            postsOfferDocumentReference.update("offer_status","declined");

                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            });

            // Offer Reddetme
            buttonDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);
                    // Offer Id elimizde var offeri sileceğiz yada pasifleştiricez.
                    // buna artı olarak kabul edildiğinde postsoffer tablsounda offer_status satırı güncellenecek.

                    // Post Offer tablosunda offer_status güncellenecek
                    postsOfferDocumentReference.update("offer_status","declined");

                }
            });

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    gaveRating((int) ratingBar.getRating());
                }
            });

        }

        public void gaveRating(Integer rate){

            final DocumentReference userInfoDocumentReference = firebaseFirestore.collection("UserInformations").document(userInfoId);
            final DocumentReference postsOfferDocumentReference = firebaseFirestore.collection("Posts_Offer").document(offerId);

            userInfoDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            final Map<String,Object> data = document.getData();
                            final String ratingCountSTR= (String) data.get("rating_count");
                            final String ratingSTR = (String) data.get("rating");

                            final Float ratingCount = Float.parseFloat(ratingCountSTR);
                            final Float rating = Float.parseFloat(ratingSTR);

                            final Float newRatingCount = ratingCount+1;
                            final Float newRating = ( ( rating * ratingCount ) + rate ) / newRatingCount;



                            final String newRatingCountSTR = Float.toString(newRatingCount);
                            final String newRatingSTR = Float.toString(newRating);

                            userInfoDocumentReference.update("rating",newRatingSTR,"rating_count",newRatingCountSTR).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    postsOfferDocumentReference.update("giving_rating","rated").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Refresh this page
                                            ((Activity)itemView.getContext()).finish();
                                            Intent postsOffersIntent = new Intent(itemView.getContext(),PostsOffersActivity.class);
                                            postsOffersIntent.putExtra("post_id",postId);
                                            postsOffersIntent.putExtra("title",postTitle);
                                            itemView.getContext().startActivity( postsOffersIntent);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
