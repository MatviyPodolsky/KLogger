package com.way.mat.klogger.ui.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;

import com.way.mat.klogger.event.Event;

import java.util.List;

public class LoggerWidget {

    private static final String TAG = LoggerWidget.class.getCanonicalName();

    private final WindowManager windowManager;
    private final LoggerView loggerView;

    private boolean shown;

    private LoggerWidget(@NonNull Builder builder) {
        this.windowManager = (WindowManager) builder.getContext().getSystemService(Context.WINDOW_SERVICE);
        this.loggerView = new LoggerView(builder);
    }

    public void show() {
        if (shown) {
            return;
        }
        shown = true;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.START | Gravity.TOP;
        windowManager.addView(loggerView, params);

        loggerView.scrollToBottom();
    }

    public void setLog(Event log){
        loggerView.setLog(log);
    }

    public void setLogs(List<Event> logs){
        loggerView.setLogs(logs);
    }

    public void hide() {
        if (!shown) {
            return;
        }

        shown = false;

        try {
            windowManager.removeView(loggerView);
        } catch (IllegalArgumentException e) {
            android.util.Log.e(TAG, "View not attached to window");
        }
    }

    public void increaseTextSize() {
        if (loggerView != null) {
            loggerView.increaseTextSize();
            loggerView.scrollToBottom();
        }
    }

    public void decreaseTextSize() {
        if (loggerView != null) {
            loggerView.decreaseTextSize();
            loggerView.scrollToBottom();
        }
    }

    public List<Event> getLogs() {
        return loggerView != null ? loggerView.getLogs() : null;
    }

    public void applyFilter(Event.TYPE filter) {
        loggerView.applyFilter(filter);
    }

    public static class Builder {

        private Context context;

        public Context getContext() {
            return context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public LoggerWidget build() {

            checkOrThrow(context);

            return new LoggerWidget(this);
        }

        private void checkOrThrow(Context context) {
            if (context == null)
                throw new IllegalArgumentException("Context cannot be null. Call LoggerWidget.Builder.setContext");
        }
    }

    public boolean isShown() {
        return shown;
    }
}
