package com.way.mat.klogger.presenter;

import com.way.mat.klogger.newtwork.RestClient;
import com.way.mat.klogger.newtwork.response.TestResponse;
import com.way.mat.klogger.view.MainView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: matviy;
 * Date: 8/29/17;
 * Time: 12:03 PM.
 */

public class MainPresenter extends BasePresenter<MainView> {

    private Disposable request;

    public void sendAPICall() {
        RestClient.getApiService()
                .sendRequest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<TestResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        request = d;
                    }

                    @Override
                    public void onNext(TestResponse value) {
                        if (getView() != null) {
                            getView().onResponseSucces(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onResponseFailded();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (request != null && !request.isDisposed()) {
                            request.dispose();
                        }
                    }

                });
    }

}
