package com.share.open_source.view.ydd;

import com.share.open_source.model.ProgramListModel;


/**
 * Created by xk on 2017/9/21.
 */

public interface IMovieListView {

    void showMovieList(ProgramListModel data);
    void onError();
}
