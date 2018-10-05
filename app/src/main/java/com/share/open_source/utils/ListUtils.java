package com.share.open_source.utils;

import java.util.List;

public class ListUtils {

    public static boolean isEmpty(List list){
        if (list == null){
            return false;
        }
        return list.size() == 0;
    }

}
