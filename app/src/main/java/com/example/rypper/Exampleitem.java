package com.example.rypper;

public class Exampleitem {
    private String mImageUrl;
    private String mCreator;
    private int mpower;
    private int mid;

    public Exampleitem(String imageUrl,String creator,int power,int id){
        mImageUrl=imageUrl;
        mCreator=creator;
        mpower=power;
        mid=id;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }

    public int getPowerCount() {
        return mpower;
    }

    public int getMid() {
        return mid;
    }
}
