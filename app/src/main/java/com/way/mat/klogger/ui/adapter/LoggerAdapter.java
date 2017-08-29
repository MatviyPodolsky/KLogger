package com.way.mat.klogger.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.way.mat.klogger.R;
import com.way.mat.klogger.event.Event;
import com.way.mat.klogger.ui.adapter.holder.LogViewHolder;
import com.way.mat.klogger.util.Globals;
import com.way.mat.klogger.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class LoggerAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private final Context activity;

    private List<Event> entities;
    private List<Event> filteredEntities;

    private int textSize = Globals.DEFAULT_TEXT_SIZE;

    private Event.TYPE filter = null;

    public LoggerAdapter(Context activity) {
        this.activity = activity;

        this.entities = new ArrayList<>();
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        Event log = getLog(position);
        switch (log.getType()) {
            case ACTION:
                holder.log.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_green_dark));
                break;
            case ERROR:
                holder.log.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_dark));
                break;
            default:
                holder.log.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
                break;
        }
        holder.log.setTextSize(textSize);
        holder.log.setText(log.toString());
    }

    public Event getLog(int position) {
        if (filteredEntities != null && filteredEntities.size() > 0) {
            return filteredEntities.get(position);
        }

        return null;
    }

    public void setLogs(List<Event> entities) {
        this.entities = entities;
        this.filteredEntities = new ArrayList<>(entities);
        notifyDataSetChanged();
    }

    public List<Event> getLogs() {
        return filteredEntities;
    }

    public void applyFilter(Event.TYPE filter) {
        this.filter = filter;
        this.filteredEntities = LogUtils.getFilteredList(entities, filter);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredEntities == null ? 0 : filteredEntities.size();
    }

    public void insertLog(Event log) {
        entities.add(log);
        if (filter == null || log.getType() == filter) {
            filteredEntities.add(log);
            notifyItemInserted(getItemCount());
        }
    }

    public void increaseTextSize() {
        textSize++;
        notifyDataSetChanged();
    }

    public void decreaseTextSize() {
        if (textSize > Globals.MINIMUM_TEXT_SIZE) {
            textSize--;
            notifyDataSetChanged();
        }
    }

}
