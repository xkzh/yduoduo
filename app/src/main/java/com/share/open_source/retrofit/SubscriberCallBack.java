package com.share.open_source.retrofit;

import com.socks.library.KLog;
import com.share.open_source.model.response.BaseResponse;

import rx.Subscriber;

/**
 * Created by xk on 2017/9/21.
 *
 * TODO 这个Callback抽取也能够公用，只需将  ResultResponse 换成 BaseResult即可，但是请求成功的判断条件逻辑需要重写 2017-9-21 17:17:45
 *
 */

public abstract class SubscriberCallBack<T> extends Subscriber<BaseResponse<T>> {


    @Override
    public void onNext(BaseResponse response) {
        boolean isSuccess = response.ok;

       /*
       TODO xk 2018-6-13 22:54:47

       if(isSuccess){
            onSuccess((T)response.data);
        }else{
            Toast.makeText(AppApplication.getContext(),response.message,Toast.LENGTH_SHORT).show();
            onFailure(response);
        }*/
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e.getLocalizedMessage());
        onError();
    }

    protected abstract void onSuccess(T response);
    protected abstract void onError();
    protected void onFailure(BaseResponse response){};

}
