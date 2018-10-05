package com.share.open_source.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.model.ProgramListModel;
import com.share.open_source.presenter.MovieListPresenter;
import com.share.open_source.ui.activity.MovieDetailActivity;
import com.share.open_source.ui.adapter.TV2Adapter;
import com.share.open_source.ui.fragment.base.BaseFragment;
import com.share.open_source.utils.AdUtils;
import com.share.open_source.utils.ListUtils;
import com.share.open_source.utils.MagneticLinkUtils;
import com.share.open_source.view.ydd.IMovieListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;


/**
 * Created by xk on 2017/9/19.
 */

public class DongManFragment extends BaseFragment<MovieListPresenter> implements IMovieListView {

    private List<ProgramModel> mDataSource = new ArrayList<>();
    TV2Adapter movieAdapter;

    @Override
    protected MovieListPresenter createPresenter() {
        return new MovieListPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_dong_man;
    }


    @Bind(R.id.fl_content)
    FrameLayout mFlContent;


    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;


    private boolean isRefresh = false;
    private boolean luomiAdsIsOpen = false;




    private int start = 0;
    private int limit = 20;
    private String category,type;


    @Override
    public void initData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public void initView(View rootView) {

        mStateView = StateView.inject(mFlContent);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.view_empty);
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new TV2Adapter(getActivity(),mDataSource);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(movieAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                start = 0;
                onHandlerRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onHandlerRefresh();
            }
        });

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() { // 点击重新加载
            @Override
            public void onRetryClick() {
                onHandlerRefresh();
            }
        });

        movieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProgramModel bean = (ProgramModel) adapter.getItem(position);

                    if(bean != null){
                        handlerGoIntent(bean);
                    }else{
                        ToastUtils.showShort("链接为空");
                    }

            }
        });


        mStateView.showLoading();
        onHandlerRefresh();
    }


    private void handlerGoIntent(ProgramModel bean){

        if(bean != null){
            if(bean.viewType == 1000){
                ToastUtils.showShort("这是广告");
            }else if(bean.viewType == 2000){
                AppApplication.joinQQGroup(getActivity(), Constants.QQ_KEY);
            }else{
                MovieDetailActivity.startActivity(mActivity,bean);
            }
        }else{
            ToastUtils.showShort("参数错误");
        }
    }


    public void onHandlerRefresh() {
        Map<String,Object> map = new HashMap<String,Object>();
        mPresenter.getMovieList(map);
    }


    private void handlerFillData(List<ProgramModel> data) {

        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        data = MagneticLinkUtils.filterMagneticLink(data);
        if(isRefresh){
            isRefresh = false;
            start = 0;
            mDataSource.clear();
        }

        if (AppApplication.isIsLuomiAdOpen()) {
            int size = data.size();
            for (int i = 1; i < size; i++)
                if (i % 5 == 0) {
                    ProgramModel adsBean = new ProgramModel(1000);
                    data.add(i, adsBean);
                }
        }
        mStateView.showContent();

        mDataSource.addAll(data);
        movieAdapter.notifyDataSetChanged();
        start += data.size();
        if (ListUtils.isEmpty(mDataSource)) {
            mStateView.showEmpty();
        }

    }



    @Override
    public void showMovieList(ProgramListModel data) {
        if (data != null) {
            handlerFillData(data.list);
        }
    }

    @Override
    public void onError() {

        if (ListUtils.isEmpty(mDataSource)) {
            //如果一开始进入没有数据
            mStateView.showRetry();//显示重试的布局
        }
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }



}
