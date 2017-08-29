package com.way.mat.klogger.presenter;


import com.way.mat.klogger.view.BaseView;
import com.way.mat.klogger.view.ViewPresenter;

public class BasePresenter<V extends BaseView> implements ViewPresenter<V> {

    private V mView;

    @Override
    public void bind(final V view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    protected final V getView() {
        return mView;
    }

}
