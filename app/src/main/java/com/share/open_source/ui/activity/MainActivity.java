package com.share.open_source.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;
import com.umeng.socialize.UMShareAPI;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.helper.BottomNavigationViewHelper;
import com.share.open_source.model.app.AppData;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.ui.fragment.DongManFragment;
import com.share.open_source.ui.fragment.MovieFragment;
import com.share.open_source.ui.fragment.TVFragment;
import com.share.open_source.ui.fragment.ZongYiFragment;
import com.share.open_source.utils.NetWorkUtils;
import com.share.open_source.utils.SpUtils;

import butterknife.Bind;

/***
 * 样式的改变还真的和其他SDK没关系，和自身的样式有关系，比如说不是宋体，就是样式配置有问题
 *
 * 暂时暂时收不到，功能没测，1/2/3 三种情况
 * GYS 太大，TBS应该能播放.m3u8，测试TBS能这种情况 如果可行替换掉，以减小包的体积
 * 微信分享审核通过，分享功能需要测试   已完成功能正常
 * 磁力链接给予提示并复制到剪切板  已完成功能正常
 *
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * TODO 应用程序启动前把排行榜的数据请求过来，缓存到本地，用于卡片数据的填充
     */
    private static String NOTIFY_TAG = "notify";

    @Bind(R.id.navigation)
    BottomNavigationView navigation;
    @Bind(R.id.mViewPager)
    ViewPager mViewPger;

    private AppData appData;
    private long exitTime;


    @Override
    public void initView() {


            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setOnNavigationItemSelectedListener(this);
            mViewPger.setOffscreenPageLimit(4);
            mViewPger.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public int getCount() {
                    return TabFragment.values().length;
                }

                @Override
                public Fragment getItem(int position) {
                    return TabFragment.values()[position].fragment();
                }
            });
            mViewPger.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    navigation.setSelectedItemId(TabFragment.values()[position].menuId);
                }
            });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            mViewPger.setCurrentItem(TabFragment.from(item.getItemId()).ordinal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private enum TabFragment {
        tuijian(R.id.navigation_tuijian, MovieFragment.class),
        shujia(R.id.navigation_shujia, TVFragment.class),
        bangdan(R.id.navigation_bangdan, ZongYiFragment.class),
        zhuti(R.id.navigation_zhuti, DongManFragment.class);


        private Fragment fragment;
        private final int menuId;
        private final Class<? extends Fragment> clazz;

        TabFragment(@IdRes int menuId, Class<? extends Fragment> clazz) {
            this.menuId = menuId;
            this.clazz = clazz;
        }

        @NonNull
        public Fragment fragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static TabFragment from(int itemId) {
            for (TabFragment fragment : values()) {
                if (fragment.menuId == itemId) {
                    return fragment;
                }
            }
            return tuijian;
        }

        public static void onDestroy() {
            for (TabFragment fragment : values()) {
                fragment.fragment = null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再次点击，退出应用",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabFragment.onDestroy();

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
        return R.layout.activity_main;
    }




}
