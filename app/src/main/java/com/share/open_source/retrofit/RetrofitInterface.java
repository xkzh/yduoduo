package com.share.open_source.retrofit;

import com.share.open_source.constants.ApiURL;
import com.share.open_source.model.app.AppData;
import com.share.open_source.model.BaseResponse;
import com.share.open_source.model.ConfigModel;
import com.share.open_source.model.SearchModel;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.model.ProgramListModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by xk on 2017/9/19.
 */

public interface RetrofitInterface {

    /**
     * 配置接口
     */
    @GET(ApiURL.APP_YDD_CONFIG)
    Call<AppData> getAppData();

    @Headers("Cache-Control: public, max-age=43200")
    @FormUrlEncoded
    @POST("")
    Observable<BaseResponse<ProgramModel>> getDetail(@FieldMap Map<String,Object> map);


    @Headers("Cache-Control: public, max-age=43200")
    @FormUrlEncoded
    @POST("")
    Observable<BaseResponse<ProgramListModel>> getLocalSearch(@Field("keyword") String keyword);



}
