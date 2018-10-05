/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.share.open_source.widget.banner;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.crazysunj.cardslideview.CardHandler;
import com.crazysunj.cardslideview.CardViewPager;
import com.crazysunj.cardslideview.ElasticCardView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.share.open_source.R;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.utils.CommonUtils;
import com.share.open_source.utils.GlideUtils;

import java.util.List;


public class BannerCardHandler implements CardHandler<ProgramModel>{

    @Override
    public View onBind(final Context context, final ProgramModel data, final int position, @CardViewPager.TransformerMode int mode) {
        View view = View.inflate(context, R.layout.module_detail_top_banner_item, null);
        KenBurnsView iv_cover = (KenBurnsView) view.findViewById(R.id.iv_cover);
        ElasticCardView cardView = (ElasticCardView) view.findViewById(R.id.banner_cv);

        ImageView iv_play = (ImageView) view.findViewById(R.id.iv_play);

        final boolean isCard = mode == CardViewPager.MODE_CARD;
        cardView.setPreventCornerOverlap(isCard);
        cardView.setUseCompatPadding(isCard);
        if(!TextUtils.isEmpty(data.images)){
            List<String> images = CommonUtils.strTranslist(data.images);
            if(images != null && images.size() > 0){
                GlideUtils.loadRoundImage(context, iv_cover, images.size() > 0 ? images.get(0) : "", R.drawable.default_bg_3_2);
            }

        }
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = CommonUtils.strTranslist(data.playSource);
                if(list != null && list.size() > 0){
                    String link = CommonUtils.strTranslist(data.playSource).get(0);
                    CommonUtils.TBSPlayer(context,link,data.title);
                }

            }
        });

        if(data.viewType != 0){
            iv_play.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        return view;
    }

    private OnAdClickListener onAdClickListener;
    public interface  OnAdClickListener{
        void onAdClick();
    }


    public void setOnAdClickListener(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
    }
}
