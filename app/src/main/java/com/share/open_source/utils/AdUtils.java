package com.share.open_source.utils;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.hz.yl.b.HHType;
import com.hz.yl.b.HhInfo;
import com.hz.yl.b.mian.HmNative;
import com.hz.yl.b.mian.NativeListener;
import com.hz.yl.b.mian.UpLoadPay;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.tencent.smtt.sdk.VideoActivity;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;

import java.util.List;
import java.util.Random;

public class AdUtils {
    
    Context mContext;
    FragmentManager fragmentManager;
    LayoutInflater inflater;

    public AdUtils(Context mContext,FragmentManager fragmentManager){
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    HmNative hmNative;

    /**
     *原生广告实体对象
     */
    HhInfo hhInfo;
    private Handler handler;

    public void showAd() {

        /**
         * 原生广告实例化
         */
        hmNative = new HmNative(mContext, String.valueOf(HHType.bookshelf) + "", new NativeListener() {

            @Override
            public void LoadSuccess(List hhInfos) {
                Message msg = handler.obtainMessage();
                msg.obj = hhInfos.get(0);
                handler.sendMessage(msg);
                for (int i = 0; i < hhInfos.size(); i++) {
                    System.out.println(">>>>原生数据视频>>>>" + hhInfos.get(i));
                }
            }

            @Override
            public void LoadError(String errorInfo) {
                System.out.println(">>>>>>>>>>>" + errorInfo);
            }
        }, 10);

        NiceDialog.init()
                .setLayoutId(R.layout.view_layout)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        holder.setOnClickListener(R.id.close, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        final ImageView imageView = holder.getView(R.id.iv_ads);
                        handler=new Handler(Looper.getMainLooper()){
                            public void handleMessage(final Message msg) {
                                ((Activity)mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hhInfo=(HhInfo) msg.obj;
                                        Glide.with(mContext).load(hhInfo.getImgurl()).into(imageView); //(KenBurnsView)helper.getView(R.id.iv_ads)
                                        UpLoadPay.getInstance().upLoadNativeShow(mContext, hhInfo);
                                    }
                                });
                            };
                        };
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                UpLoadPay.getInstance().upLoadNativeClick(mContext, hhInfo);
                            }
                        });
                    }
                })
                .setWidth(210)
                .setOutCancel(false)
                .setAnimStyle(R.style.EnterExitAnimation)
                .show(fragmentManager);


    }





}
