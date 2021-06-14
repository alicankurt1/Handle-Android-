package com.alican.graduationproject1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostRecycleViewAdapter extends RecyclerView.Adapter<PostRecycleViewAdapter.PostHolder> {

    private final ArrayList<String> imageUrlList;
    private final ArrayList<String> titleList;
    private final ArrayList<String> categoryList;
    //private ArrayList<String> explanationList;
    private final ArrayList<String> workDateList;
    private final ArrayList<String> startTimeList;
    private final ArrayList<String> finishTimeList;
    //private ArrayList<String> addressList;
    //private ArrayList<String> latitudeList;
    //private ArrayList<String> longitudeList;

    private final ArrayList<String> paymentList;
    private final OnPostListener mOnPostListener;

    public PostRecycleViewAdapter(ArrayList<String> imageUrlList,ArrayList<String> titleList, ArrayList<String> categoryList, ArrayList<String> workDateList, ArrayList<String> startTimeList, ArrayList<String> finishTimeList,ArrayList<String> paymentList, OnPostListener onPostListener) {
        this.imageUrlList = imageUrlList;
        this.titleList = titleList;
        this.categoryList = categoryList;
        this.workDateList = workDateList;
        this.startTimeList = startTimeList;
        this.finishTimeList = finishTimeList;
        this.paymentList = paymentList;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycleview_posts,parent,false);

        return new PostHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.title.setText(titleList.get(position));
        holder.category.setText(categoryList.get(position));
        holder.workDate.setText(workDateList.get(position));
        holder.startTime.setText(startTimeList.get(position));
        holder.finishTime.setText(finishTimeList.get(position));
        holder.payment.setText(paymentList.get(position));
        // ücret gösterilecek
        Picasso.get().load(imageUrlList.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    static class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final TextView title;
        private final TextView category;
        private final TextView workDate;
        private final TextView startTime;
        private final TextView finishTime;
        private final TextView payment;
        private final OnPostListener onPostListener;


        public PostHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewRecyclePost);
            title = itemView.findViewById(R.id.textViewRecycleTitle);
            category = itemView.findViewById(R.id.textViewRecycleCategory);
            workDate = itemView.findViewById(R.id.textViewRecycleWorkDate);
            startTime = itemView.findViewById(R.id.textViewRecycleStartTime);
            finishTime = itemView.findViewById(R.id.textViewRecycleFinishTime);
            payment = itemView.findViewById(R.id.textViewRecyclePayment);

            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostListener{
        void onPostClick(int position);
    }

}
