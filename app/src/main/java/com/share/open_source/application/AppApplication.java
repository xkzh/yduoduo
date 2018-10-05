package com.share.open_source.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.share.open_source.R;
import com.share.open_source.constants.Constants;
import com.share.open_source.constants.HostType;
import com.share.open_source.model.app.AppData;
import com.share.open_source.retrofit.RetrofitManager;
import com.share.open_source.utils.ACache;
import com.share.open_source.utils.AppUtils;
import com.share.open_source.utils.NetWorkUtils;
import com.share.open_source.utils.SharedPreferencesUtil;
import com.share.open_source.utils.SpUtils;


import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class AppApplication extends BaseApplication {
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, R.color.app_theme_color);
                return new ClassicsHeader(context).setDrawableSize(16).setTextSizeTitle(14).setProgressResource(R.drawable.animation_loading_frame);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.black, R.color.app_theme_color);
                return new ClassicsFooter(context).setDrawableSize(16).setTextSizeTitle(14);
            }
        });
    }

    private static AppApplication sInstance;
    protected static Context context;
    protected static Handler handler;
    protected static int mainThreadId;
    public static boolean luomiOpen = false;


    public static int beDivide = 10;

    public static int beXinBaDivide = 10;


    public static int adTime = 30000;

    public static boolean isIsLuomiAdOpen() {
        return luomiOpen;
    }


    public static void setIsLuomiAdOpen(boolean isLuomiAdOpen) {
        AppApplication.luomiOpen = isLuomiAdOpen;
    }

    public static void setAdTime(int time) {
        AppApplication.adTime = time;
    }

    public static void setbeDivide(int count) {
        AppApplication.beDivide = count;
    }

    public static void setBeXinBaDivide(int beXinBaDivide) {
        AppApplication.beXinBaDivide = beXinBaDivide;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();


        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
        changeFontTypaFace();
        Utils.init(this);
        AppUtils.init(this);

        initAd();
        umengInit();
        initTBS();
        initPrefs();

        if (!NetWorkUtils.isWifiProxy(this)) {
            getAppData();//获取app配置数据
        }
    }



    private void initTBS() {

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }


    private void initAd() {

    }

    private void umengInit() {

    }

    public void getAppData() {
        Call<AppData> call = RetrofitManager.getInstance(HostType.APP_CONFIG).getRetrofitInterface().getAppData();
        call.enqueue(new Callback<AppData>() {
            @Override
            public void onResponse(Call<AppData> call, Response<AppData> response) {
                if (response != null && response.body() != null) {
                    AppData appData = response.body();
                    if (appData != null) {

                    }
                    SpUtils.setObject(Constants.JSON_API_DATA, appData);
                }
            }

            @Override
            public void onFailure(Call<AppData> call, Throwable t) {

            }
        });
    }


    public static AppApplication getsInstance() {
        return sInstance;
    }

    public static ACache mAcache;
    public static Gson mGson;

    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    public static ACache getAcache() {
        if (mAcache == null) {
            mAcache = ACache.get(sInstance);
        }
        return mAcache;
    }

    public static Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    Typeface mtypeface;

    public Typeface getFangZhengSong3() {

        if (mtypeface == null) {
            mtypeface = Typeface.createFromAsset(getAssets(), "fangzhengsongsan.ttf");
        }
        return mtypeface;
    }


    public void changeFontTypaFace() {
        Typeface fangZhengSong3 = Typeface.createFromAsset(getAssets(), "fangzhengsongsan.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, fangZhengSong3);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取上下文对象
     *
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    public static boolean joinQQGroup(Context mContext, String key) {

        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            mContext.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
