package com.share.open_source.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.share.open_source.widget.glide.GlideCircleTransform;
import com.share.open_source.widget.glide.GlideRoundTransform;


public class GlideUtils {

    public static void loadImage(Context mContext, ImageView view, String imgUrl, int placeHolderRes) {
        Glide.with(mContext).load(imgUrl).placeholder(placeHolderRes).into(view);
    }

    public static void loadRoundImage(Context mContext, ImageView view, String imgUrl, int placeHolderRes) {
        Glide.with(mContext).load(imgUrl).placeholder(placeHolderRes).transform(new GlideRoundTransform(mContext)).into(view);
    }

    public static void loadCircleImage(Context mContext, ImageView view, String imgUrl, int placeHolderRes) {
        Glide.with(mContext).load(imgUrl).placeholder(placeHolderRes).transform(new GlideCircleTransform(mContext)).into(view);
    }

}
