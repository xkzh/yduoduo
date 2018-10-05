package com.share.open_source.ui.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.nukc.stateview.StateView;
import com.umeng.analytics.MobclickAgent;
import com.share.open_source.R;
import com.share.open_source.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by xk on 2017/9/19.
 */

public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment {

    protected T mPresenter;
    private View rootView;
    protected StateView mStateView; //用于显示加载中、网络异常，空布局、内容布局
    protected Activity mActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getContentViewLayoutID(), container, false);
            ButterKnife.bind(this, rootView);
            mStateView = StateView.inject(getStateViewRoot());
            if (mStateView != null) {
                mStateView.setLoadingResource(R.layout.page_loading);
                mStateView.setRetryResource(R.layout.page_net_error);
            }

            initView(rootView);
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * 初始化view
     *
     * @param rootView
     */
    public void initView(View rootView) {
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /**
     * 设置listener的操作
     */
    public void initListener() {
    }

    //创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int getContentViewLayoutID();


    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     *
     * @return
     */
    public View getStateViewRoot() {
        return rootView;
    }

    @Override
    protected void onFragmentFirstVisible() {
        //当第一次可见的时候，加载数据
    }

    public void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        rootView = null;
    }

    /**
     * 判断是否注册EventBus
     *
     * @param subscribe
     * @return
     */
    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    /**
     * 如果没有注册EventBus,进行注册操作
     *
     * @param subscribe
     */
    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    /**
     * 如果已经注册EventBus,则进行解除注册
     *
     * @param subscribe
     */
    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}





























