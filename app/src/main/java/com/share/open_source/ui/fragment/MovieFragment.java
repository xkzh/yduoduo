package com.share.open_source.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.blankj.utilcode.util.ToastUtils;
import com.share.open_source.model.app.ShareBean;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.app.AppData;
import com.share.open_source.model.CategoryModel;
import com.share.open_source.model.DataSource;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.ui.activity.MovieSearchActivity;
import com.share.open_source.ui.activity.YddSettingActivity;
import com.share.open_source.ui.adapter.MyFragmentPagerAdapter;
import com.share.open_source.ui.fragment.base.BaseFragment;
import com.share.open_source.utils.ShareUtils;
import com.share.open_source.utils.SpUtils;
import com.share.open_source.widget.ColorFlipPagerTitleView;
import com.share.open_source.widget.ScrollTextView;
import com.share.open_source.widget.righttopmenu.MenuItem;
import com.share.open_source.widget.righttopmenu.TopRightMenu;

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
import butterknife.OnClick;
public class MovieFragment extends BaseFragment {

    @Bind(R.id.iv_menu)
    AppCompatImageView iv_menu;

    @Bind(R.id.iv_search)
    AppCompatImageView iv_search;


    TopRightMenu mTopRightMenu;

    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    @Bind(R.id.scrollTextView)
    ScrollTextView scrollTextView;

    private String paomadeng;

    List<CategoryModel> modelList = new ArrayList<CategoryModel>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private ShareBean shareBean;
    private AppData appData;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_movie;
    }

    @OnClick(R.id.iv_menu)
    public void menu() {
        initPopupWindow();

    }

    @OnClick(R.id.iv_search)
    public void search() {
        startActivity(new Intent(getActivity(), MovieSearchActivity.class));
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
        appData = SpUtils.getObject(Constants.JSON_API_DATA, AppData.class);
        if (appData != null) {
            shareBean = appData.share;

        }
        modelList.clear();
        modelList.addAll(DataSource.getMovieCategory(getActivity()));
    }

    private void initView() {
        if (!TextUtils.isEmpty(paomadeng)) {
            scrollTextView.setVisibility(View.VISIBLE);
            scrollTextView.setText(paomadeng);
        }

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
            MovieCategoryFragment newsListFragment = createListFragments(model);
            mFragmentList.add(newsListFragment);
            channelNames.add(model.title);
        }
    }

    private MovieCategoryFragment createListFragments(CategoryModel config) {
        MovieCategoryFragment fragment = new MovieCategoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), channelNames, mFragmentList);
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.setAdapter(adapter);
    }

    private void initMagicIndicator7() {
        magicIndicator.setBackgroundColor(Color.parseColor("#fafafa"));
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
                simplePagerTitleView.setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                simplePagerTitleView.setSelectedColor(Color.parseColor("#13B9C7"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
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
        magicIndicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initPopupWindow() {

        mTopRightMenu = new TopRightMenu(getActivity());
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.ic_search, "分享app"));
        menuItems.add(new MenuItem(R.drawable.ic_search, "加入官方群"));
        menuItems.add(new MenuItem(R.drawable.ic_search, "设置"));

        mTopRightMenu
                .showIcon(false)
                .dimBackground(true)
                .needAnimationStyle(true)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                shareApp();
                                break;
                            case 1:
                                if (!TextUtils.isEmpty(Constants.QQ_KEY)) {
                                    AppApplication.joinQQGroup(getActivity(), Constants.QQ_KEY);//加入QQ群
                                }
                                break;
                            case 2:
                                startActivity(new Intent(mActivity, YddSettingActivity.class));
                                break;
                        }


                    }
                })
                .showAsDropDown(iv_menu, -iv_menu.getWidth() - 10, -10);

    }


    private void shareApp() {
        if (shareBean == null) {
            shareBean = new ShareBean("影多多开源项目", "影多多开源项目,欢迎入群（823516112）", "https://www.lanzous.com/b311881/", "");
        }

        ShareUtils.onekeyShare(getActivity(), shareBean, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                showToast(mActivity, "分享成功！");
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                showToast(mActivity, "分享失败！");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                showToast(mActivity, "分享取消！");
            }
        });

    }


}




