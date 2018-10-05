package com.share.open_source.utils;

import android.text.TextUtils;

import com.share.open_source.model.ProgramModel;

import java.util.ArrayList;
import java.util.List;

public class MagneticLinkUtils {

    public static List<ProgramModel> filterMagneticLink(List<ProgramModel> list){
        List<ProgramModel> result = new ArrayList<>();
        if(list == null){
            return result;
        }
        for(ProgramModel model : list){
            if(!TextUtils.isEmpty(model.playSource)){
               List<String> linkList =  CommonUtils.strTranslist(model.playSource);
               if(linkList != null && linkList.size() > 0){
                   String link = linkList.get(0);
                    if(!TextUtils.isEmpty(link)){ // 判断不为空
                            if(!(link.endsWith("rar") || link.endsWith("exe") || link.endsWith("html") || link.endsWith("htm"))){ // 判断非rar、exe、html、htm
                                if(!link.endsWith("null")){ // 有些链接 是以null结尾的导致无法播放
                                    result.add(model);
                                }
                            }
                    }
               }
            }
        }
        return result;
    }

}
