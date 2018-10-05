package com.share.open_source.model;

import android.content.Context;

import com.share.open_source.R;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static List<CategoryModel> getMovieCategory(Context mContext){

        List<CategoryModel> list = new ArrayList<CategoryModel>();

        list.add(new CategoryModel(mContext.getString(R.string.txt_type0)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type1)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type2)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type5)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type3)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type4)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type8)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type9)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type20)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type21)));
        return list;
    }


    public static List<CategoryModel> getTVCategory(Context mContext){

        List<CategoryModel> list = new ArrayList<CategoryModel>();

        list.add(new CategoryModel(mContext.getString(R.string.txt_type0)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type22)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type23)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type24)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type25)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type26)));
        list.add(new CategoryModel(mContext.getString(R.string.txt_type27)));

        return list;
    }

}
