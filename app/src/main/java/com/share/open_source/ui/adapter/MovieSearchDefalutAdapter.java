package com.share.open_source.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.share.open_source.R;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.SearchDefaultItem;
import com.share.open_source.model.app.HotkeywordBean;
import com.share.open_source.utils.SpUtils;
import com.share.open_source.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;


public class MovieSearchDefalutAdapter extends BaseQuickAdapter<SearchDefaultItem,BaseViewHolder> {

    private Context mContext;


    private static final int HOT_KEYWORD = 300;

    private static final int SEARCH_RECORD = 100;
    int hotIndex = 0;

    public MovieSearchDefalutAdapter(Context mContext, List<SearchDefaultItem> list){
        super(list);
        this.mContext = mContext;
        setMultiTypeDelegate(new MultiTypeDelegate<SearchDefaultItem>() {
            @Override
            protected int getItemType(SearchDefaultItem item) {
                int viewType = item.viewType;
               if(viewType == 2){
                    return HOT_KEYWORD;
                }else if(viewType == 3){
                    return SEARCH_RECORD;
                }

                return HOT_KEYWORD;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(HOT_KEYWORD,R.layout.module_movie_search_keyword_recommend)
                .registerItemType(SEARCH_RECORD,R.layout.module_movie_search_keyword_recommend);

    }



    @Override
    protected void convert(BaseViewHolder helper, final SearchDefaultItem item) {
        switch (helper.getItemViewType()){

            case HOT_KEYWORD:

                helper.setText(R.id.tv_type_tag,item.title);

                final FlowLayout flowLayout = helper.getView(R.id.flowLayout);
                flowLayout.setColorful(true);
                final List<HotkeywordBean> mTagList = item.hotWordList;
                showHotWord(mTagList,flowLayout);

                flowLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
                    @Override
                    public void TagClick(HotkeywordBean bean) {
                        onTagClickListener.onTagClick(bean);
                    }
                });
                break;
            case SEARCH_RECORD:

                helper.setText(R.id.tv_type_tag,item.title);

                final FlowLayout recordLayout = helper.getView(R.id.flowLayout);
                recordLayout.setColorful(true);
                final List<String> searchList = item.searchRecord;

                List<HotkeywordBean> searchRecord = new ArrayList<>();
                for(String str : searchList){
                    searchRecord.add(new HotkeywordBean(str,""));
                }

                showHotWord(searchRecord,recordLayout);

                recordLayout.setOnTagClickListener(new FlowLayout.OnTagClickListener() {
                    @Override
                    public void TagClick(HotkeywordBean bean) {
                        onTagClickListener.onTagClick(bean);
                    }
                });

                break;

        }
    }

    /**
     * 每次显示8个热搜词
     */
    private synchronized void showHotWord(List<HotkeywordBean> tagList,FlowLayout flowLayout) {
        flowLayout.cleanTag();
        int tagSize = tagList.size();
//        String[] tags = new String[tagSize];
        List<HotkeywordBean> tags = new ArrayList<>();
        for (int j = 0; j < tagSize && j < tagList.size(); hotIndex++, j++) {
//            tags[j] = tagList.get(hotIndex % tagList.size());
            tags.add(tagList.get(hotIndex % tagList.size()));
        }

        flowLayout.setData(tags);
    }

    private OnTagClickListener onTagClickListener;
    public interface  OnTagClickListener{
        void onTagClick(HotkeywordBean bean);
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

}
