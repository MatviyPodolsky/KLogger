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

import java.util.ArrayList;
import java.util.List;

public class LoggerAdapter extends RecyclerView.Adapter<LogViewHolder> {

    private final Context activity;

    private List<Event> entities;

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
        holder.log.setText(log.toString());
    }

    public Event getLog(int position) {
        if (entities != null && entities.size() > 0) {
            return entities.get(position);
        }

        return null;
    }

    public void setLogs(List<Event> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return entities == null ? 0 : entities.size();
    }

    public void insertLog(Event log) {
        entities.add(log);
        notifyItemInserted(getItemCount());
    }
}
