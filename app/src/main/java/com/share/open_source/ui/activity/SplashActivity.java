package com.share.open_source.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hz.yl.b.mian.HaSplash;
import com.hz.yl.b.mian.SplashListener;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.app.AppData;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.statusbar.Eyes;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.utils.GlideUtils;
import com.share.open_source.utils.NetWorkUtils;
import com.share.open_source.utils.SpUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by xk on 2017/9/19.
 */

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private ImageView banner_view;


    private RelativeLayout rel_luomi;
    private AppData appData;
    private boolean luomiAdIsOpen = false;
    private String splashImgUrl;

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isTaskRoot()){
            finish();
            return;
        }
        Eyes.translucentStatusBar(this, false);//
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        banner_view = (ImageView) findViewById(R.id.banner_view);
        rel_luomi = (RelativeLayout) findViewById(R.id.rel_luomi);


        initDatas();
        if (NetWorkUtils.isNetworkAvailable(mContext)) {
            if (AppApplication.isIsLuomiAdOpen()) { // luomiAdIsOpen
                initLuoMiScreen();
            } else {
                noAds(true, 3000);
            }
        } else {
            noAds(true, 3000);
        }


    }


    private void initDatas() {
        if (SpUtils.getObject(Constants.JSON_API_DATA, AppData.class) != null) {
            appData = SpUtils.getObject(Constants.JSON_API_DATA, AppData.class);
            if (appData != null) {

            } else {
                AppApplication.getsInstance().getAppData();
            }
        } else {
            AppApplication.getsInstance().getAppData();
        }
    }

    private boolean isJunmp = false;

    private void noAds(boolean loadImg, long time) {
        if (loadImg) {
            //            Glide.with(this).load(splashImgUrl).into(banner_view);
            GlideUtils.loadImage(this, banner_view, splashImgUrl, 0);
        }

        if (!isJunmp) {
            isJunmp = true;
            Observable.timer(time, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            readyGoThenKill(MainActivity.class);
                        }
                    });
        }

    }

    private void initLuoMiScreen() {
        /**
         * 实例化开屏广告
         */
        new HaSplash(SplashActivity.this, rel_luomi, null, new SplashListener() {

            @Override
            public void onPresent() {
                // TODO 广告展示完成  自行处理跳转Main逻辑
                //                showToast(mContext, "广告展示完成");
                noAds(false, 1);
            }

            @Override
            public void onNoAD(String errorInfo) {
                // TODO Auto-generated method stub
                //                showToast(mContext, "没有获得广告");
                noAds(false, 1000);
                System.out.println(">>>>>>>>>没有获得广告");
            }

            @Override
            public void OnClicked() {
                // TODO Auto-generated method stub
                //                showToast(mContext, "广告被点击");
                System.out.println(">>>>>>>>>广告被点击");
            }
        }, true);
    }


}
