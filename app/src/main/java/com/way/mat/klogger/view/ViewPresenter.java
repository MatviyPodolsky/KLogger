package com.way.mat.klogger.view;

public interface ViewPresenter<V extends BaseView> {

    void bind(final V view);

    void unbind();

}
