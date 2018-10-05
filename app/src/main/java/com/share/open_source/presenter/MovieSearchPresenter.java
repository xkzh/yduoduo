package com.share.open_source.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.HostType;
import com.share.open_source.model.BaseResponse;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.model.ProgramListModel;
import com.share.open_source.retrofit.RetrofitManager;
import com.share.open_source.view.ydd.IMovieSearchView;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xk on 2017/9/21.
 */

public class MovieSearchPresenter extends BasePresenter<IMovieSearchView> {
    public MovieSearchPresenter(IMovieSearchView view) {
        super(view);
    }


    public void loadSearchResultList(String query) {
        mView.showSearchResult(initJson().list);
    }

    private ProgramListModel initJson() {

        try {
            // TODO 传参，根据不同的参数读不同的json

            StringBuffer stringBuffer = new StringBuffer();
            InputStream inputStream = AppApplication.getsInstance().getAssets().open("other.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) != -1){
                stringBuffer.append(new String(buf, 0, len, "utf-8"));
            }
            inputStream.close();

            ProgramListModel model = new ProgramListModel();
            Gson gson = new Gson();
            List<ProgramModel> mList = gson.fromJson(stringBuffer.toString(),new TypeToken<List<ProgramModel>>() {}.getType());
            Collections.shuffle(mList);
            model.list = mList;
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
