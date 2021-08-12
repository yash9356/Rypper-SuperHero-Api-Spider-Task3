package com.example.rypper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<Exampleitem> mExampleList;

    public ExampleAdapter(Context context,ArrayList<Exampleitem> exampleList){
        mContext= context;
        mExampleList=exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item,parent,false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ExampleAdapter.ExampleViewHolder holder, int position) {
        Exampleitem currentItem= mExampleList.get(position);

        String imageUrl= currentItem.getImageUrl();
        String creatorName =currentItem.getCreator();
        int power =currentItem.getPowerCount();
        int id=currentItem.getMid();

        holder.mTextViewCreator.setText(creatorName);
        holder.mTextViewLikes.setText("Power: "+power);
        holder.mTextid.setText(Integer.toString(id));
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext,InfoActivity.class);
                intent.putExtra("ID",id);

                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;
        public TextView mTextid;

        public ExampleViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            mTextid=itemView.findViewById(R.id.id);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator =itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);
        }
    }
}