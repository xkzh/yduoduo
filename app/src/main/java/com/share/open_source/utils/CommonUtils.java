package com.share.open_source.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.share.open_source.ui.activity.PlayContentActivity;

import java.util.ArrayList;
import java.util.List;


public class CommonUtils {
    public static void TBSPlayer(Context mContext, String link,String movieName) {
        Intent intent = new Intent(mContext, PlayContentActivity.class);
        intent.putExtra("playUrl", link);
        if(!TextUtils.isEmpty(movieName)){
            intent.putExtra("movieName", movieName);
        }
        mContext.startActivity(intent);

    }

    public static List<String> strTranslist(String link){
        if(!TextUtils.isEmpty(link) && link.length() > 2){
            String[] mArr = link.substring(1, link.length() - 1).split(",");
            if(mArr != null && mArr.length > 0){
                List<String> list = new ArrayList<>();
                for(int i = 0; i < mArr.length; i++){
                    if(!TextUtils.isEmpty(mArr[i])){
                        list.add(mArr[i].trim());
                    }
                }
                return list;
            }else{
                return null;
            }
        }
        return null;
    }

}
