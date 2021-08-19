package com.example.rypper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>{
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
        String CharacterName =currentItem.getCreator();
        int power =currentItem.getPowerCount();
        int id=currentItem.getMid();
        boolean checked=MainActivity.getInstance().CheckSelected(id);
        if(checked){
            holder.mStarHero1.setBackgroundResource(R.drawable.star4);
        }else {
            holder.mStarHero1.setBackgroundResource(R.drawable.emptyimg);
        }
        holder.mTextViewCreator.setText(CharacterName);
        holder.mTextViewLikes.setText("Power: "+power);
        holder.mTextid.setText(Integer.toString(id));
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
        holder.mStarHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.getInstance(), "added to favorite", Toast.LENGTH_SHORT).show();
                MainActivity.getInstance().SaveFavHero(id,CharacterName,power,imageUrl);
                holder.mStarHero1.setBackgroundResource(R.drawable.star4);


            }
        });
        holder.mImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InfoActivity.class);
                intent.putExtra("ID",id);
                intent.putExtra("HeroName",CharacterName);
                intent.putExtra("ImgUrl",imageUrl);
                Pair<View, String> p1 = Pair.create((View)holder.mImageView, "example_transition");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, p1);
                mContext.startActivity(intent,options.toBundle());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewCreator;
        public TextView mTextViewLikes;
        public TextView mTextid;
        public ImageView mImageView,mStarHero,mStarHero1;

        public ExampleViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            mTextid=itemView.findViewById(R.id.id);
            mImageView = itemView.findViewById(R.id.image_view);
            mStarHero=itemView.findViewById(R.id.StarHero);
            mTextViewCreator =itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);
            mStarHero1=itemView.findViewById(R.id.StarHero2);
        }
    }
}
