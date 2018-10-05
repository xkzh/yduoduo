package com.share.open_source.presenter;

import com.share.open_source.constants.HostType;
import com.share.open_source.model.BaseResponse;
import com.share.open_source.model.ProgramModel;
import com.share.open_source.retrofit.RetrofitManager;
import com.share.open_source.view.ydd.IDetailView;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xk on 2017/9/21.
 */

public class DetailPresenter extends BasePresenter<IDetailView> {
    public DetailPresenter(IDetailView view) {
        super(view);
    }
    private Disposable mBannerDisposable;

    public void loadDetail(Map<String,Object> map) {
        Subscription rxSubscription = RetrofitManager.getInstance(HostType.YING_D_D).getRetrofitInterface().getDetail(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ProgramModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError();
                    }

                    @Override
                    public void onNext(BaseResponse<ProgramModel> result) {
                        ProgramModel bean = result.getResult();
                        if(result.getCode() == 1000){
                            mView.showDetail(bean);
                        }else{
                            mView.onError();
                        }

                    }
                });
        addSubscription(rxSubscription);
    }



    public void startBanner() {
        if (mBannerDisposable == null) {
            mBannerDisposable = Flowable.interval(3, TimeUnit.SECONDS, io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<Long>() {
                        @Override
                        public void onNext(Long aLong) {

                            // V1.0.0   Attempt to invoke interface method 'void com.xkcn.ydd.view.ydd.IDetailView.switchBanner()' on a null object reference
                            try {
                                mView.switchBanner();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void endBanner() {
        if (mBannerDisposable == null || mBannerDisposable.isDisposed()) {
            return;
        }
        mBannerDisposable.dispose();
        mBannerDisposable = null;
    }

}
