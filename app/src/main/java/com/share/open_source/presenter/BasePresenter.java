package com.share.open_source.presenter;

import com.share.open_source.constants.HostType;
import com.share.open_source.retrofit.RetrofitInterface;
import com.share.open_source.retrofit.RetrofitManager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by xk on 2017/9/19.
 */

public abstract class BasePresenter<V> {

    protected V mView;
    private CompositeSubscription mCompositeSubscription;

    public BasePresenter(V view){
        attachView(view);
    }

    public void attachView(V view){
        mView = view;
    }

    public void detachView(){
        mView = null;
        onUnsubscribe();
    }

    //取消RXjava注册，以避免内存泄露
    public void onUnsubscribe(){
        if(mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription){
        if(mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }

        mCompositeSubscription.add(subscription);

/*
      TODO  xk 2018-6-13 22:47:27

      mCompositeSubscription.add(observable.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(subscriber));
        */
    }


}
