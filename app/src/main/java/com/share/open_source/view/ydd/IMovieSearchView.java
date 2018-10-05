package com.share.open_source.view.ydd;

import com.share.open_source.model.ProgramModel;

import java.util.List;



public interface IMovieSearchView {

    void showSearchResult(List<ProgramModel> data);
    void onError();

}
