package com.share.open_source.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.share.open_source.application.AppApplication;
import com.share.open_source.model.ProgramListModel;
import com.tencent.smtt.sdk.WebView;
import com.share.open_source.R;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.SearchDefaultItem;
import com.share.open_source.model.SearchResultItem;
import com.share.open_source.model.app.AppData;
import com.share.open_source.model.app.HotkeywordBean;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.presenter.MovieSearchPresenter;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.ui.adapter.MovieSearchAdapter;
import com.share.open_source.ui.adapter.MovieSearchDefalutAdapter;
import com.share.open_source.utils.AppCommonUtils;
import com.share.open_source.utils.CommonUtils;
import com.share.open_source.utils.ListUtils;
import com.share.open_source.utils.MagneticLinkUtils;
import com.share.open_source.utils.SpUtils;
import com.share.open_source.view.ydd.IMovieSearchView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class MovieSearchActivity extends BaseActivity<MovieSearchPresenter> implements IMovieSearchView {

    @Bind(R.id.fl_content)
    FrameLayout mFlContent;

    MovieSearchAdapter mSearchResultAdapter;
    MovieSearchDefalutAdapter mDefalutAdapter;

    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Bind(R.id.defaultRecylerView)
    RecyclerView defaultRecylerView;

    @Bind(R.id.et_search)
    EditText et_search;

    @Bind(R.id.iv_clear)
    ImageView iv_clear;

    @Bind(R.id.searchResultRecylerView)
    RecyclerView searchResultRecylerView;
    @Bind(R.id.adWeb)
    WebView webView;
    private List<SearchResultItem> mResultList = new ArrayList<SearchResultItem>();
    private List<SearchDefaultItem> mDefaultDataList = new ArrayList<SearchDefaultItem>();

    private List<String> recordList = new ArrayList<>();

    @Override
    protected MovieSearchPresenter createPresenter() {
        return new MovieSearchPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.module_search_activity;
    }


    @Override
    public void initView() {

        initDefaultAdapter();
        initSearchAdapter();
        handlerFetchHotKeyword();
        initSearchRecord();
        initSearchView();
    }


    private void initSearchView() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(et_search.getText().toString());
                    handlerSearchRecord();
                    return true;
                }
                return false;
            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable str) {
                iv_clear.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(str)) {
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                    mFlContent.setVisibility(View.GONE);
                    defaultRecylerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void handlerSearchRecord() {
        String keyword = et_search.getText().toString();
        if(!TextUtils.isEmpty(keyword)){
            if (!recordList.contains(keyword)) {
                // 在记录中不存在才执行添加操作
                if (recordList.size() > 8) {
                    // 删除最后一条数据，将数据添加到recordList中
                    recordList.remove(recordList.size() - 1);
                }
                recordList.add(0, keyword);
            }
            SpUtils.setDataList(Constants.SEARCH_RECORD,recordList);
        }
        mDefalutAdapter.notifyDataSetChanged();
    }

    private void initDefaultAdapter() {

        mDefalutAdapter = new MovieSearchDefalutAdapter(this,mDefaultDataList);
        mDefalutAdapter.isFirstOnly(true);
        mDefalutAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        defaultRecylerView.setLayoutManager(new LinearLayoutManager(this));
        defaultRecylerView.setAdapter(mDefalutAdapter);
        mDefalutAdapter.setOnTagClickListener(new MovieSearchDefalutAdapter.OnTagClickListener() {
            @Override
            public void onTagClick(HotkeywordBean bean) {

                if(!TextUtils.isEmpty(bean.playurl)){
                    CommonUtils.TBSPlayer(mContext,bean.playurl,bean.keyword);
                }else{
                    search(bean.keyword);
                    et_search.setText(bean.keyword);
                }
            }
        });

    }

    private void search(final String keyword) {
        if(TextUtils.isEmpty(keyword)){
            ToastUtils.showShort("请输入关键字");
            return;
        }
        AppCommonUtils.hideSoftInput(et_search);
        defaultRecylerView.setVisibility(View.GONE);
        mFlContent.setVisibility(View.VISIBLE);

        mResultList.clear();
        mStateView.showLoading();
        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                mPresenter.loadSearchResultList(keyword);
            }
        });

        mPresenter.loadSearchResultList(keyword);
    }



    private void initSearchAdapter(){

        mStateView = StateView.inject(mFlContent);
        if(mStateView != null){
            mStateView.setLoadingResource(R.layout.page_loading);
            mStateView.setRetryResource(R.layout.page_net_error);
            mStateView.setEmptyResource(R.layout.view_empty);
        }

        mSearchResultAdapter = new MovieSearchAdapter(this,mResultList);
        mSearchResultAdapter.isFirstOnly(true);
        mSearchResultAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        searchResultRecylerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultRecylerView.setAdapter(mSearchResultAdapter);

    }


    private void handlerFetchHotKeyword() {

        AppData appData = initAppDataJson();
        if(initAppDataJson() != null){
            List<HotkeywordBean> hotWordlist =  appData.hotkeyword;
            if(hotWordlist != null && hotWordlist.size() > 0){
                defaultRecylerView.setVisibility(View.VISIBLE);

                Collections.shuffle(hotWordlist);
                SearchDefaultItem item = new SearchDefaultItem();
                item.viewType = 2;
                item.title = "热搜关键词";
                item.hotWordList = hotWordlist;
                mDefaultDataList.add(item);
            }
        }

        mDefalutAdapter.notifyDataSetChanged();
    }

    private void initSearchRecord() {
        List<String> list = SpUtils.getDataList(Constants.SEARCH_RECORD,String.class);
        if(list != null){
            recordList.addAll(list);
        }
        if(recordList.size() > 0){

            SearchDefaultItem item = new SearchDefaultItem();
            item.viewType = 3;
            item.title = "搜索记录";
            item.searchRecord = recordList;
            mDefaultDataList.add(item);
        }

        mDefalutAdapter.notifyDataSetChanged();
    }



    @Override
    public void showSearchResult(List<ProgramModel> list) {

        mStateView.showContent();
        if(list != null){

            List<ProgramModel> result = MagneticLinkUtils.filterMagneticLink(list);

            List<ProgramModel> normalList = new ArrayList<>();
            List<ProgramModel> magneticLinkList = new ArrayList<>();
            for(ProgramModel model : result){
                if(!TextUtils.isEmpty(model.playSource)){
                    List<String> linkList =  CommonUtils.strTranslist(model.playSource);
                    if(linkList != null && linkList.size() > 0){
                        String link = linkList.get(0);
                        if(!TextUtils.isEmpty(link)){ // 判断不为空
                            if (!(link.startsWith("ftp") || link.startsWith("thunder") || link.startsWith("ed2k") || link.startsWith("magnet") || link.startsWith("torrent"))) { // 判断非磁力链接
                                normalList.add(model);
                            }else{
                                magneticLinkList.add(model);
                            }
                        }
                    }
                }
            }

            if(normalList.size() > 0){
                SearchResultItem item = new SearchResultItem();
                item.viewType = 1;
                item.list = normalList;
                mResultList.add(item);
            }
            if(magneticLinkList.size() > 0){
                SearchResultItem item = new SearchResultItem();
                item.viewType = 2;
                item.list = magneticLinkList;
                mResultList.add(item);
            }
        }

        if (ListUtils.isEmpty(mResultList)) {
            mStateView.showEmpty();
        }
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError() {
        if (ListUtils.isEmpty(mResultList)) {
            //如果一开始进入没有数据
            mStateView.showRetry();//显示重试的布局
        }
    }


    private AppData initAppDataJson() {

        try {
            // TODO 传参，根据不同的参数读不同的json

            StringBuffer stringBuffer = new StringBuffer();
            InputStream inputStream = AppApplication.getsInstance().getAssets().open("searchrecord.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) != -1){
                stringBuffer.append(new String(buf, 0, len, "utf-8"));
            }
            inputStream.close();


            Gson gson = new Gson();
            AppData model = gson.fromJson(stringBuffer.toString(),AppData.class);


            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}






















