package com.share.open_source.ui.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.github.nukc.stateview.StateView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.share.open_source.R;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.widget.SharePopupWindow;
import com.share.open_source.widget.X5WebView;

import butterknife.Bind;
import butterknife.OnClick;

public class PlayContentActivity extends BaseActivity implements SharePopupWindow.ShareTypeListener {

    private String palyUrl,movieName;
    @Bind(R.id.x5_webview)
    X5WebView x5webView;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_share)
    TextView tv_share;

    @Bind(R.id.fl_content)
    FrameLayout mFlContent;

    SharePopupWindow popupWindow;

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.tv_share)
    public void share(){
        if (popupWindow == null) {
            popupWindow = new SharePopupWindow(this);
            popupWindow.setShareTypeListener(this);
        }
        popupWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);


        palyUrl = getIntent().getStringExtra("playUrl");
        movieName = getIntent().getStringExtra("movieName");
        if (palyUrl == null) {
            ToastUtils.showShort("视频链接为空");
            return;
        }

        if(!TextUtils.isEmpty(movieName)){
            tv_title.setText(movieName);
        }

        mStateView = StateView.inject(mFlContent);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.view_empty);
        }
        mStateView.showLoading();
        x5webView.loadUrl(palyUrl);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        x5webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        x5webView.setWebChromeClient(new WebChromeClient(){});
        x5webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                mStateView.showContent();
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mStateView.showContent();
            }
        });

    }



    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_play_content;
    }


    @Override
    protected void onDestroy() {
        x5webView.clearCache(true);
        x5webView.clearHistory();
        super.onDestroy();
    }

    @Override
    public void onShare(ImageView view, String type) {
        SHARE_MEDIA share_media = null;
        if(TextUtils.equals(type,"qq")){
            share_media = SHARE_MEDIA.QQ;
        }else if(TextUtils.equals(type,"qq_zone")){
            share_media = SHARE_MEDIA.QZONE;
        }else if(TextUtils.equals(type,"weixin")){
            share_media = SHARE_MEDIA.WEIXIN;
        }else{
            share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }


        UMImage  image = new UMImage(mContext, R.mipmap.ydd);

        image.setThumb(image);
        UMWeb web = new UMWeb(palyUrl); //  palyUrl  "https://www2.yuboyun.com/hls/2018/07/13/KqG3bCng/playlist.m3u8"
        if(!TextUtils.isEmpty(movieName)){
            web.setTitle("【"+getString(R.string.app_name)+"】"+movieName);//标题 // movieName  "邪不压正"
        }else{
            web.setTitle("最新电影来自【"+getString(R.string.app_name)+"】的分享");//标题
        }

        web.setThumb(image);  //缩略图
        String share_content = "点击即可观看，加群(823516112)获取更多最新电影";
        web.setDescription(share_content);//描述

        new ShareAction(PlayContentActivity.this).setPlatform(share_media).setCallback(umShareListener)
                .withMedia(web)
                .share();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Log.e("xk","--------"+t.getMessage());
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {}
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
