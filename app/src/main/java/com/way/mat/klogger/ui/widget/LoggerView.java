package com.way.mat.klogger.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.way.mat.klogger.event.Event;
import com.way.mat.klogger.ui.adapter.LoggerAdapter;

import java.util.List;

@SuppressLint("ViewConstructor")
public class LoggerView extends LinearLayout {

    private final Context context;

    private RecyclerView recyclerView;
    private ScrollingLayoutManager manager;
    private LoggerAdapter adapter;

    public LoggerView(LoggerWidget.Builder builder) {
        super(builder.getContext());
        this.context = builder.getContext();
        setupView();
    }

    private void setupView() {
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        manager = new ScrollingLayoutManager(context);
        manager.setScrollEnabled(true);

        adapter = new LoggerAdapter(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        addView(recyclerView);
    }

    public void setLog(Event log) {
        if (adapter != null) {
            adapter.insertLog(log);
            scrollToBottom();
        }
    }

    public void setLogs(List<Event> logs) {
        if (adapter != null) {
            adapter.setLogs(logs);
            scrollToBottom();
        }
    }

    public List<Event> getLogs() {
        return adapter != null ? adapter.getLogs() : null;
    }

    public void applyFilter(Event.TYPE filter) {
        adapter.applyFilter(filter);
    }

    public void scrollToBottom() {
        if (recyclerView != null &&  adapter != null && adapter.getItemCount() > 0) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    public void increaseTextSize() {
        if (adapter != null) {
            adapter.increaseTextSize();
        }
    }

    public void decreaseTextSize() {
        if (adapter != null) {
            adapter.decreaseTextSize();
        }
    }

}
