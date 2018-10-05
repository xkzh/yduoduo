package com.share.open_source.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.share.open_source.R;
import com.share.open_source.model.ProgramModel;


import java.util.List;


public class EpisodeAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    private List<ProgramModel> data;
    private static final int MOVIE = 0;


    public EpisodeAdapter(Context mContext, List<String> list){
        super(list);
        this.mContext = mContext;
        setMultiTypeDelegate(new MultiTypeDelegate<String>() {
            @Override
            protected int getItemType(String item) {
                    return MOVIE;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(MOVIE, R.layout.module_dsj_every_episode_list_item);

    }

    @Override
    protected void convert(BaseViewHolder helper, String bean) {
        int position = helper.getAdapterPosition();

        helper.setText(R.id.tv_title,"第"+(position)+"集");

    }
}
