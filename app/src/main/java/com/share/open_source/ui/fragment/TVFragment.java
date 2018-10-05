package com.share.open_source.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.model.app.AppData;
import com.share.open_source.model.CategoryModel;
import com.share.open_source.model.DataSource;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.ui.adapter.MyFragmentPagerAdapter;
import com.share.open_source.ui.fragment.base.BaseFragment;
import com.share.open_source.widget.ColorFlipPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by xk on 2017/9/19.
 */

public class TVFragment extends BaseFragment{


    @Bind(R.id.tv_indicator)
    MagicIndicator tv_indicator;

    @Bind(R.id.tv_view_pager)
    ViewPager tv_view_pager;

    List<CategoryModel> modelList = new ArrayList<CategoryModel>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    private AppData appData;
    private String key;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_tv;
    }



    @Override
    public void initData() {

        initJson();
        initView();
        initMagicIndicator7();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    private void initJson() {
        modelList.clear();
        modelList.addAll(DataSource.getTVCategory(getActivity()));
    }

    private void initView() {
        List<String> channelNames = new ArrayList<>();

        for (CategoryModel model : modelList) {
            channelNames.add(model.title);
        }
        if (modelList != null) {
            setNewsList(modelList, channelNames);
            setViewPager(channelNames);
        }

    }

    private void setNewsList(List<CategoryModel> modelList, List<String> channelNames) {
        mFragmentList.clear();
        for (CategoryModel model : modelList) {
            TVCategoryFragment newsListFragment = createListFragments(model);
            mFragmentList.add(newsListFragment);
            channelNames.add(model.title);
        }
    }

    private TVCategoryFragment createListFragments(CategoryModel config) {


        TVCategoryFragment fragment = new TVCategoryFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), channelNames, mFragmentList);
        tv_view_pager.setAdapter(adapter);
    }

    private void initMagicIndicator7() {
        tv_indicator.setBackgroundColor(Color.parseColor("#fafafa"));
        CommonNavigator commonNavigator7 = new CommonNavigator(getActivity());
        commonNavigator7.setScrollPivotX(0.65f);
        commonNavigator7.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return modelList == null ? 0 : modelList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(modelList.get(index).title);
                simplePagerTitleView.setNormalColor(R.color.black);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#13B9C7"));
                simplePagerTitleView.setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_view_pager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 3)); //  指示器的高度
                indicator.setLineWidth(UIUtil.dip2px(context, 10)); //  指示器的宽度
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));//  指示器的圆角
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#13B9C7"));
                return indicator;
            }
        });
        tv_indicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(tv_indicator, tv_view_pager);
    }





}




