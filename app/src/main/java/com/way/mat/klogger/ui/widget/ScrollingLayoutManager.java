package com.way.mat.klogger.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class ScrollingLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled;

    public ScrollingLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        isScrollEnabled = scrollEnabled;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
