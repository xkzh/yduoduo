package com.share.open_source.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.chaychan.lib.SlidingLayout;
import com.github.nukc.stateview.StateView;
import com.umeng.analytics.MobclickAgent;
import com.share.open_source.R;
import com.share.open_source.listener.PermissionListener;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.statusbar.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BGASwipeBackHelper.Delegate{

    protected T mPresenter;
    protected StateView mStateView;
    public PermissionListener mPermissionListener;
    protected Bundle saveInstanceState;
    public static List<Activity> mActivitys = new LinkedList();
    protected Context mContext;
    protected BGASwipeBackHelper mSwipeBackHelper;
    private static Activity mCurrentActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        if(enableSlideClose()){
            SlidingLayout slidingLayout = new SlidingLayout(this);
            slidingLayout.bindActivity(this);
        }

        this.saveInstanceState = savedInstanceState;
        mContext = this;
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        synchronized (mActivitys){
            mActivitys.add(this);
        }
        mPresenter = createPresenter();
        ButterKnife.bind(this);
        initView();
        initzlistener();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.StatusBarLightMode(this);
        }
    }


    public void initView(){}


    public void initzlistener(){}

    public boolean enableSlideClose(){
        return true;
    }


    protected  abstract T createPresenter();

    protected abstract int getContentViewLayoutID();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mCurrentActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivitys){
            mActivitys.remove(this);
        }
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }



    @Override
    public void onBackPressed() {
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }


    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length > 0){
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i = 0;i < grantResults.length;i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }

                    if(deniedPermissions.isEmpty()){
                        mPermissionListener.onGranted();
                    }else{
                        mPermissionListener.onDenied(deniedPermissions);
                    }

                }
            break;

        }
    }

    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }


    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        mSwipeBackHelper.setSwipeBackEnable(true);
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        mSwipeBackHelper.setIsWeChatStyle(true);
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        mSwipeBackHelper.setIsNeedShowShadow(true);
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    @Override
    public void onSwipeBackLayoutCancel() {
    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    public void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}

















