package com.way.mat.klogger.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.way.mat.klogger.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvLog)
    public TextView log;

    public LogViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

}