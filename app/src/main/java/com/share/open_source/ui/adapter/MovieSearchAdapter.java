package com.share.open_source.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.SearchResultItem;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.ui.activity.MovieDetailActivity;

import java.util.List;

public class MovieSearchAdapter extends BaseQuickAdapter<SearchResultItem,BaseViewHolder> {

    private static final int NORMAL_RESULT = 1;
    private static final int MAG_RESULT = 2;

    public MovieSearchAdapter(Context mContext, List<SearchResultItem> list){
        super(list);
        this.mContext = mContext;
        setMultiTypeDelegate(new MultiTypeDelegate<SearchResultItem>() {
            @Override
            protected int getItemType(SearchResultItem item) {

                if (item.viewType == 1) {
                    return NORMAL_RESULT;
                } else {
                    return MAG_RESULT;
                }
            }
        });

        getMultiTypeDelegate()
                .registerItemType(NORMAL_RESULT, R.layout.module_movie_search_recycle)
                .registerItemType(MAG_RESULT, R.layout.module_movie_search_recycle);
    }

    @Override
    protected void convert(final BaseViewHolder helper, SearchResultItem bean) {


        switch (bean.viewType){

            case NORMAL_RESULT:


                ImageView iv_result_tag = helper.getView(R.id.iv_result_tag);
                iv_result_tag.setImageResource(R.drawable.ic_normal);
                RecyclerView recycleView = helper.getView(R.id.recycleView);
                recycleView.setLayoutManager(new LinearLayoutManager(mContext));
                helper.setText(R.id.tv_type_tag,"播放资源(共 "+bean.list.size()+" 条结果)");
                if(bean.list.size() > 5){
                    if (AppApplication.isIsLuomiAdOpen()) {
                        int size = bean.list.size();
                        for (int i = 1; i < size; i++)
                            if (i % 5 == 0) {  // TODO 加在数据的头部 2018-7-14 13:34:57
                                ProgramModel adsBean = new ProgramModel(1000);
                                bean.list.add(i, adsBean);
                            }
                    }
                }
                MovieSearchCategoryAdapter normalAdapter =  new MovieSearchCategoryAdapter(mContext,bean.list);
                recycleView.setAdapter(normalAdapter);

                normalAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {

                        ProgramModel bean = (ProgramModel) baseQuickAdapter.getItem(position);

                        if(bean != null){
                            handlerGoIntent(bean);
                        }else{
                            ToastUtils.showShort("链接为空");
                        }
                    }
                });

                break;
            case MAG_RESULT:
                ImageView iv_magnet_tag = helper.getView(R.id.iv_result_tag);
                iv_magnet_tag.setImageResource(R.drawable.ic_magnet);
                RecyclerView magRecycleView = helper.getView(R.id.recycleView);
                magRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
                helper.setText(R.id.tv_type_tag,"磁力资源(共 "+bean.list.size()+" 条结果)");
                if(bean.list.size() > 5){
                    if (AppApplication.isIsLuomiAdOpen()) {
                        int size = bean.list.size();
                        for (int i = 1; i < size; i++)
                            if (i % 5 == 0) {  // TODO 加在数据的头部 2018-7-14 13:34:57
                                ProgramModel adsBean = new ProgramModel(1000);
                                bean.list.add(i, adsBean);
                            }
                    }
                }
                MovieSearchCategoryAdapter magAdapter =  new MovieSearchCategoryAdapter(mContext,bean.list);
                magRecycleView.setAdapter(magAdapter);
                magAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {

                        ProgramModel data = (ProgramModel) baseQuickAdapter.getItem(position);
                        if(data != null){
                            handlerGoIntent(data);
                        }else{
                            ToastUtils.showShort("链接为空");
                        }
                    }
                });
                break;
        }
    }


    private void handlerGoIntent(ProgramModel bean){

        if(bean != null){
            if(bean.viewType == 2000){
                AppApplication.joinQQGroup(mContext, Constants.QQ_KEY);
            }else{
                MovieDetailActivity.startActivity(mContext,bean);
            }
        }else{
            ToastUtils.showShort("参数错误");
        }
    }

}
