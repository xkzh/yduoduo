package com.share.open_source.model.response;

/**
 * Created by xk on 2017/9/21.
 */

public class BaseResponse<T> {

    public boolean ok;


    public BaseResponse(boolean isOk){
        ok = isOk;
    }

}
