package com.share.open_source.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.crazysunj.cardslideview.CardViewPager;
import com.github.nukc.stateview.StateView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.smtt.sdk.WebView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.app.AppData;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.presenter.DetailPresenter;
import com.share.open_source.statusbar.StatusBarUtil;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.ui.adapter.EpisodeAdapter;
import com.share.open_source.utils.AdUtils;
import com.share.open_source.utils.CommonUtils;
import com.share.open_source.utils.ListUtils;
import com.share.open_source.utils.SpUtils;
import com.share.open_source.view.ydd.IDetailView;
import com.share.open_source.widget.SharePopupWindow;
import com.share.open_source.widget.banner.BannerCardHandler;
import com.share.open_source.widget.banner.WrapBannerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

public class MovieDetailActivity extends BaseActivity<DetailPresenter> implements IDetailView,SharePopupWindow.ShareTypeListener {

    public static final String INTENT_BEAN = "intent_bean";
    ProgramModel bean;

    @Bind(R.id.fl_content)
    FrameLayout mFlContent;

    @Bind(R.id.tv_title)
    TextView tv_title;
    TextView tv_movie_title;


    TextView tv_translate_title;
    TextView tv_director;
    TextView tv_performer;
    TextView tv_length;
    TextView tv_date_area;
    TextView tv_intro;


    RelativeLayout rl_card_container;


    CardViewPager mHomeBanner;
    WrapBannerView mWrapBanner;

    @Bind(R.id.episode_recyclerView)
    RecyclerView episode_recyclerView;
    EpisodeAdapter mAdapter;
    @Bind(R.id.adWeb)
    WebView webView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    View headView;
    SharePopupWindow popupWindow;
    private List<String> mDataSource = new ArrayList<>();

    protected StateView mStateView;
    private String param_url;
    private BannerCardHandler cardHandler;
    private AppData appData;
    String share_title,share_content,share_link,share_image,juJi="";

    public static void startActivity(Context context, ProgramModel bean) {
        context.startActivity(new Intent(context, MovieDetailActivity.class)
                    .putExtra(INTENT_BEAN,bean));
    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.tv_share)
    public void share(){
        if (popupWindow == null) {
            popupWindow = new SharePopupWindow(this);
            popupWindow.setShareTypeListener(this);
        }
        popupWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);
    }


    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public void initView() {
        initAdapter();
        initData();
    }
    public void initData() {
        bean = (ProgramModel) getIntent().getSerializableExtra(INTENT_BEAN);
        cardBannerList.clear();
        if(bean == null){

        }else{
            handlerFillData(bean);
        }
    }




    public void initAdapter() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.StatusBarLightMode(this);
        }

        mStateView = StateView.inject(mFlContent);
        if (mStateView != null) {
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.view_empty);
        }


        episode_recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new EpisodeAdapter(this,mDataSource);
        episode_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        episode_recyclerView.setAdapter(mAdapter);

        headView = getLayoutInflater().inflate(R.layout.header_view_movie_detail,null);
        tv_movie_title = (TextView)headView.findViewById(R.id.tv_movie_title);

        tv_translate_title = (TextView)headView.findViewById(R.id.tv_translate_title);
        tv_director = (TextView)headView.findViewById(R.id.tv_director);
        tv_performer = (TextView)headView.findViewById(R.id.tv_performer);
        tv_length = (TextView)headView.findViewById(R.id.tv_length);
        tv_date_area = (TextView)headView.findViewById(R.id.tv_date_area);
        tv_intro = (TextView)headView.findViewById(R.id.tv_intro);


        rl_card_container = (RelativeLayout)headView.findViewById(R.id.rl_card_container);
        mHomeBanner = (CardViewPager)headView.findViewById(R.id.home_vp);
        mWrapBanner = (WrapBannerView)headView.findViewById(R.id.wrap_banner);

        mAdapter.addHeaderView(headView);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO  加个大逻辑判断，如果允许开启广告，就开启，不允许就直接调走
                Random rand = new Random();
                int randomNum = rand.nextInt(10);
                String link = (String) adapter.getItem(position);
                juJi = " 第"+(position+1)+"集";
                if(AppApplication.isIsLuomiAdOpen()&& randomNum!=0 && randomNum % AppApplication.beDivide == 0){
                    new AdUtils(mContext,getSupportFragmentManager()).showAd();
                }else{
                    if(!TextUtils.isEmpty(link)){
                        handlerGoPlay(link);
                    }else{
                        ToastUtils.showShort("链接为空");
                    }
                }

            }
        });
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() { // 点击重新加载
            @Override
            public void onRetryClick() {
                onHandlerRefresh();
            }
        });

        mWrapBanner.setOnBannerSlideListener(new WrapBannerView.OnBannerSlideListener() {
            @Override
            public void onSlide(boolean isCanSlide) {
                handleWrapBanner(isCanSlide);
            }
        });

        cardHandler = new BannerCardHandler();

    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    public void onHandlerRefresh() {
        if(TextUtils.isEmpty(param_url)){
            ToastUtils.showShort(R.string.params_error);
            return;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        mPresenter.loadDetail(map);
    }

    @Override
    public void showDetail(ProgramModel data) {
        if(data != null){
            handlerFillData(data);
        }

    }
    List<ProgramModel> cardBannerList = new ArrayList<>();
    public void handlerFillData(ProgramModel data){
       mStateView.showContent();
        if(bean == null){
            ToastUtils.showShort(R.string.params_error);
            return;
        }
        cardBannerList.add(data);
        mHomeBanner.bind(getSupportFragmentManager(), cardHandler, cardBannerList);



        tv_title.setText(data.title);
        tv_movie_title.setText("片 名："+data.title);
        if(!TextUtils.isEmpty(data.translateTitle)){
            tv_translate_title.setText("译 名："+data.translateTitle);
        }else{
            tv_translate_title.setVisibility(View.GONE);
        }

        tv_director.setText("导 演："+(!TextUtils.isEmpty(data.director)?(data.director) : "未知"));
        tv_performer.setText( "主 演："+(!TextUtils.isEmpty(data.actor) ? data.actor : "未知"));
        if(!TextUtils.isEmpty(data.minutes)){
            tv_length.setText("片 长："+data.minutes+" 分钟");
        }else{
            tv_length.setVisibility(View.GONE);
        }

        tv_date_area.setText("更 多："
                +((!TextUtils.isEmpty(data.publishDate) ? data.publishDate : "未知")
                + "  |  "
                + (!TextUtils.isEmpty(data.area) ? data.area : "未知")
                + "  |  "
                + (!TextUtils.isEmpty(data.style) ? data.style : "未知")));

        if(!TextUtils.isEmpty(data.describtion)){
            tv_intro.setText(data.describtion);
        }else{
            rl_card_container.setVisibility(View.GONE);
        }


        List<String> linksList = CommonUtils.strTranslist(data.playSource);
        if(linksList != null && linksList.size() > 0){
            share_link = linksList.get(0);
            if(linksList.size() > 1){
                mFlContent.setVisibility(View.VISIBLE);
                mDataSource.clear();
                mDataSource.addAll(linksList);
            }
        }

        share_title = data.title;
        share_content = data.describtion;

        List<String> images = CommonUtils.strTranslist(data.images);
        if(images != null && images.size() > 0){
            share_image = images.get(0);
        }
    }

    /**
     * TODO 跳转到播放界面，这里进行广告处理
     * @param link
     */
    private void handlerGoPlay(String link) {
        CommonUtils.TBSPlayer(mContext,link,share_title+juJi);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.endBanner();
    }

    @Override
    public void onError() {
        if (ListUtils.isEmpty(mDataSource)) {
            //如果一开始进入没有数据
            mStateView.showRetry();//显示重试的布局
        }
    }

    @Override
    public void switchBanner() {
        mHomeBanner.setCurrentItem(mHomeBanner.getCurrentItem() + 1, true);
    }

    private void handleWrapBanner(boolean isCanSlide) {
        if (isCanSlide) {
            mPresenter.startBanner();
        } else {
            mPresenter.endBanner();
        }
    }



    @Override
    public void onShare(ImageView view, String type) {

       SHARE_MEDIA share_media = null;
       if(TextUtils.equals(type,"qq")){
           share_media = SHARE_MEDIA.QQ;
       }else if(TextUtils.equals(type,"qq_zone")){
           share_media = SHARE_MEDIA.QZONE;
       }else if(TextUtils.equals(type,"weixin")){
           share_media = SHARE_MEDIA.WEIXIN;
       }else{
           share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
       }

        if (SpUtils.getObject(Constants.JSON_API_DATA, AppData.class) != null) {
            appData = SpUtils.getObject(Constants.JSON_API_DATA, AppData.class);
            if (appData == null) {
                AppApplication.getsInstance().getAppData();
            }
        }

       UMImage image = null;
       if(!TextUtils.isEmpty(share_image)){
           image = new UMImage(mContext, share_image);
       }else{
           image = new UMImage(mContext, R.mipmap.ydd);
       }
       image.setThumb(image);
       UMWeb web = new UMWeb(share_link);
       web.setTitle("【"+getString(R.string.app_name)+"】"+share_title);//标题
       web.setThumb(image);  //缩略图
       web.setDescription(TextUtils.isEmpty(share_content) ? share_title : share_content);//描述

       new ShareAction(MovieDetailActivity.this).setPlatform(share_media).setCallback(umShareListener)
               .withMedia(web)
               .share();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {}
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
