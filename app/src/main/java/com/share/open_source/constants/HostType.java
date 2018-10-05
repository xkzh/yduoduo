package com.share.open_source.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HostType {

    /**
     * 多少种Host类型
     */
    public static final int TYPE_COUNT = 3;




    /**
     * 新闻的host
     */
    public static final int APP_CONFIG = 3;



    /**
     * yingdd的host
     */
    public static final int YING_D_D = 5;

    /**
     * ROOT 的host
     */
    public static final int ROOT = 6;

    /**
     * 替代枚举的方案，使用IntDef保证类型安全
     */
    @IntDef({APP_CONFIG,YING_D_D,ROOT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {

    }

}
