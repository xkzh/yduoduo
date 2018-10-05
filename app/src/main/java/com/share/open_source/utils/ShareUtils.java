package com.share.open_source.utils;

import android.app.Activity;

import com.share.open_source.model.app.ShareBean;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.share.open_source.R;

public class ShareUtils {
    public static void onekeyShare(Activity activity, ShareBean shareBean, UMShareListener listener) {

        UMImage thumb = new UMImage(activity, R.mipmap.ydd); //缩略图

        UMWeb web = new UMWeb(shareBean.getTitleUrl());
        web.setTitle(shareBean.getTitle());//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(shareBean.getContent());//描述

        new ShareAction(activity)
                .withText(shareBean.getTitle())
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(listener)
                .open();
    }
}
