package com.share.open_source.view.ydd;

import com.share.open_source.model.ProgramModel;


/**
 * Created by xk on 2017/9/21.
 */

public interface IDetailView {

    void showDetail(ProgramModel data);
    void onError();
    void switchBanner();
}
