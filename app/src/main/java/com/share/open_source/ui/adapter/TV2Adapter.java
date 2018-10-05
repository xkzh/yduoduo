package com.share.open_source.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.hz.yl.b.HHType;
import com.hz.yl.b.HhInfo;
import com.hz.yl.b.mian.HmNative;
import com.hz.yl.b.mian.NativeListener;
import com.hz.yl.b.mian.UpLoadPay;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.utils.CommonUtils;
import com.share.open_source.utils.GlideUtils;

import java.util.List;

public class TV2Adapter extends BaseQuickAdapter<ProgramModel,BaseViewHolder> {

    private static final int MOVIE = 0;
    private static final int AD = 1000;

    HmNative hmNative;
    Handler handler;
    /**
     * 原生广告实体对象
     */
    HhInfo advertisement;

    public TV2Adapter(Context mContext, List<ProgramModel> list){
        super(list);
        this.mContext = mContext;
        setMultiTypeDelegate(new MultiTypeDelegate<ProgramModel>() {
            @Override
            protected int getItemType(ProgramModel item) {
                if(item.viewType == 1000){
                    return AD;
                }else{
                    return MOVIE;
                }

            }
        });

        getMultiTypeDelegate()
                .registerItemType(MOVIE, R.layout.module_movie_list_item)
                .registerItemType(AD, R.layout.module_movie_list_ad_item);

    }

    @Override
    protected void convert(final BaseViewHolder helper, ProgramModel bean) {

        switch (bean.viewType){

            case MOVIE:
                if(!TextUtils.isEmpty(bean.images)){
                    List<String> images = CommonUtils.strTranslist(bean.images);
                    if(images != null && images.size() > 0){
                        GlideUtils.loadRoundImage(helper.itemView.getContext(), (ImageView)helper.getView(R.id.iv_cover), images.size() > 0 ? images.get(0) : "", R.drawable.default_bg);
                    }
                }

                ((TextView)helper.getView(R.id.tv_title)).setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                ((TextView)helper.getView(R.id.tv_translate_title)).setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                ((TextView)helper.getView(R.id.tv_director)).setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                ((TextView)helper.getView(R.id.tv_performer)).setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                ((TextView)helper.getView(R.id.tv_length)).setTypeface(AppApplication.getsInstance().getFangZhengSong3());
                ((TextView)helper.getView(R.id.tv_date_area)).setTypeface(AppApplication.getsInstance().getFangZhengSong3());


                helper.setText(R.id.tv_title,(!TextUtils.isEmpty(bean.title)?(bean.title) : "未知"));
                if(!TextUtils.isEmpty(bean.translateTitle)){
                    helper.setText(R.id.tv_translate_title, "译 名："+bean.translateTitle);
                }else{
                    helper.getView(R.id.tv_translate_title).setVisibility(View.GONE);
                }

                helper.setText(R.id.tv_director, "导 演："+(!TextUtils.isEmpty(bean.director)?(bean.director) : "未知"));
                helper.setText(R.id.tv_performer, "主 演："+(!TextUtils.isEmpty(bean.actor) ? bean.actor : "未知"));
                if(!TextUtils.isEmpty(bean.minutes)){
                    helper.setText(R.id.tv_length, "片 长："+(!TextUtils.isEmpty(bean.minutes) ? bean.minutes+" 分钟" : "未知"));
                }else{
                    helper.getView(R.id.tv_length).setVisibility(View.GONE);
                }

                helper.setText(R.id.tv_date_area, "更 多："
                        +((!TextUtils.isEmpty(bean.publishDate) ? bean.publishDate : "未知")
                        + "  |  "
                        + (!TextUtils.isEmpty(bean.area) ? bean.area : "未知")
                        + "  |  "
                        + (!TextUtils.isEmpty(bean.style) ? bean.style : "未知")));
                break;

            case AD:
                /**
                 * 原生广告实例化
                 */
                hmNative = new HmNative(mContext, String.valueOf(HHType.bookshelf) + "", new NativeListener() {

                    @Override
                    public void LoadSuccess(List hhInfos) {
                        Message msg = handler.obtainMessage();
                        msg.obj = hhInfos.get(0);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void LoadError(String errorInfo) {
                    }
                }, 10);


                RelativeLayout rl_ad_layout = helper.getView(R.id.rl_ad_layout);
                rl_ad_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpLoadPay.getInstance().upLoadNativeClick(mContext, advertisement);
                    }
                });

                if(!TextUtils.isEmpty(bean.translateTitle)){
                    helper.setText(R.id.tv_translate_title, "译 名：未知");
                }else{
                    helper.getView(R.id.tv_translate_title).setVisibility(View.GONE);
                }

                helper.setText(R.id.tv_director, "导 演：未知");
                helper.setText(R.id.tv_performer, "主 演：未知");
                if(!TextUtils.isEmpty(bean.minutes)){
                    helper.setText(R.id.tv_length, "片 长：未知");
                }else{
                    helper.getView(R.id.tv_length).setVisibility(View.GONE);
                }



                handler = new Handler() {
                    public void handleMessage(Message msg) {
                        advertisement = (HhInfo) msg.obj;
                        if(!((Activity)mContext).isFinishing()){
                            Glide.with(mContext).load(advertisement.getImgurl()).into((ImageView)helper.getView(R.id.iv_cover));
                            helper.setText(R.id.tv_title,"片 名："+advertisement.getWenzi());
                            helper.setText(R.id.tv_date_area,"更 多："+advertisement.getWenzi2() );
                        }
                        UpLoadPay.getInstance().upLoadNativeShow(mContext, advertisement);
                    }
                };

                break;

        }


    }
}
