package com.share.open_source.retrofit;

import android.util.SparseArray;

import com.share.open_source.constants.ApiURL;
import com.share.open_source.constants.HostType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by xk on 2017/9/19.
 * <p>
 * TODO  疑点： cookie持久化 这一点尚未验证  xk 2017-9-21 18:34:52
 */

public class RetrofitManager {

    private static RetrofitManager mRetrofitManager;
    private final Retrofit mRetrofit;
    private OkHttpClient mClient;
    private RetrofitInterface mRetrofitInterface;
    private static SparseArray<RetrofitManager> sRetrofitManager = new SparseArray<>(HostType.TYPE_COUNT);

    public RetrofitManager(@HostType.HostTypeChecker int hostType) {


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//请求/响应行 + 头 + 体

        mClient = new OkHttpClient.Builder()
                //                .addInterceptor(new HeaderInterceptor())//添加头部信息拦截器
                                .addInterceptor(loggingInterceptor)//添加log拦截器  new LogInterceptor()
                //                .cache(cache)
                .connectTimeout(30 * 1000, TimeUnit.SECONDS)
                .readTimeout(30 * 1000, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiURL.getHost(hostType))
                .client(mClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);
    }

    public static RetrofitManager getInstance(int hostType) {
        mRetrofitManager = sRetrofitManager.get(hostType);
        if (mRetrofitManager == null) {
            synchronized (Object.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager(hostType);
                    sRetrofitManager.put(hostType, mRetrofitManager);
                }
            }
        }
        return mRetrofitManager;
    }

    public RetrofitInterface getRetrofitInterface() {
        return mRetrofitInterface;
    }


    /**
     * 请求访问的Request 和 Response 拦截器
     */
    private class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            okhttp3.Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            //            KLog.e("----------   Request Begin   ----->");
            //            KLog.e("| " + request.toString());
            //            KLog.json("| Response:" + content);
            //            KLog.e("----------   Request Stop:" + duration + "毫秒----->   ");
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }

    /**
     * 头部信息拦截器
     */
    private class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
            builder.addHeader("Cache-Control", "max-age=0");
            builder.addHeader("Upgrade-Insecure-Requests", "1");
            builder.addHeader("X-Requested-With", "XMLHttpRequest");
            builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
            return chain.proceed(builder.build());
        }
    }


}
