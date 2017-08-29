package com.way.mat.klogger.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.way.mat.klogger.R;
import com.way.mat.klogger.event.Event;
import com.way.mat.klogger.newtwork.ApiService;
import com.way.mat.klogger.newtwork.RestClient;
import com.way.mat.klogger.newtwork.response.TestResponse;
import com.way.mat.klogger.presenter.MainPresenter;
import com.way.mat.klogger.ui.view.CustomTextWatcher;
import com.way.mat.klogger.ui.widget.LoggerWidget;
import com.way.mat.klogger.util.EmailUtils;
import com.way.mat.klogger.util.LogUtils;
import com.way.mat.klogger.util.ValidationUtils;
import com.way.mat.klogger.view.MainView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView {

    public static final String TAG = "KLogger";

    public static final int OVERLAY_PERMISSION_REQUEST = 10001;

    @BindView(R.id.logs)
    TextView tvLogs;

    private LoggerWidget widget;

    private MainPresenter mPresenter;

    private Event.TYPE currentFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter();
        mPresenter.bind(this);
        initOverlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
    }

    private void initOverlay() {
        widget = new LoggerWidget.Builder()
                .setContext(this)
                .build();
        widget.setLogs(new ArrayList<Event>());
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.action_refresh)
    public void writeLogs() {
        addLogAndUpdate(new Event("write logs clicked", Event.TYPE.REGULAR));
    }

    @OnClick(R.id.action_api_call)
    public void callAPI() {
        mPresenter.sendAPICall();
        addLogAndUpdate(new Event("Sending request " + RestClient.BASE_URL + ApiService.ROUTE, Event.TYPE.ACTION));
    }

    @OnClick(R.id.action_toggle_overlay)
    public void toggleOverlay() {
        addLogAndUpdate(new Event("toggle overlay clicked", Event.TYPE.REGULAR));
        if (widget.isShown()) {
            widget.hide();
        } else {
            if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))) {
                widget.show();
            } else {
                Log.d(TAG, "Can't change state! Device does not have drawOverlays permissions!");
                addLogAndUpdate(new Event("Device does not have drawOverlays permissions!", Event.TYPE.ERROR));
                checkOverlayPermission();
            }
        }
    }

    @OnClick(R.id.action_increase_text_size)
    public void increaseTextSize() {
        if (widget != null) {
            widget.increaseTextSize();
        }
    }

    @OnClick(R.id.action_decrease_text_size)
    public void decreaseTextSize() {
        if (widget != null) {
            widget.decreaseTextSize();
        }
    }

    @OnClick(R.id.action_clear_logs)
    public void clearLogs() {
        clear();
    }

    @OnClick(R.id.action_filter)
    public void showFilterDialog() {
        addLogAndUpdate(new Event("show filters clicked", Event.TYPE.ACTION));
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_filter, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select filter:")
                .setView(view)
                .create();
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.filter_all:
                        currentFilter = null;
                        break;
                    case R.id.filter_action:
                        currentFilter = Event.TYPE.ACTION;
                        break;
                    case R.id.filter_error:
                        currentFilter = Event.TYPE.ERROR;
                        break;
                    case R.id.filter_regular:
                        currentFilter = Event.TYPE.REGULAR;
                        break;
                }
                applyFilter();
                dialog.dismiss();
            }
        };
        view.findViewById(R.id.filter_all).setOnClickListener(listener);
        view.findViewById(R.id.filter_action).setOnClickListener(listener);
        view.findViewById(R.id.filter_regular).setOnClickListener(listener);
        view.findViewById(R.id.filter_error).setOnClickListener(listener);

        dialog.show();
    }

    private void applyFilter() {
        addLogAndUpdate(new Event("new filter applied: " + (currentFilter != null ? currentFilter.toString() : "ALL"), Event.TYPE.REGULAR));
        widget.applyFilter(currentFilter);
    }

    @OnClick(R.id.action_email)
    public void sendEmail() {
        addLogAndUpdate(new Event("send logs clicked", Event.TYPE.ACTION));
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_dialog, null);
        final AppCompatEditText etEmail = view.findViewById(R.id.et_email);
        etEmail.addTextChangedListener(new CustomTextWatcher(etEmail));

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter email:")
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (ValidationUtils.validateEmail(etEmail)) {
                            case EMPTY_EMAIL:
                                addLogAndUpdate(new Event("empty email", Event.TYPE.ERROR));
                                break;
                            case INVALID_EMAIL:
                                addLogAndUpdate(new Event("invalid email", Event.TYPE.ERROR));
                                break;
                            case SUCCESS:
                                EmailUtils.sendEmail(MainActivity.this, etEmail.getText().toString(), LogUtils.getLogsString(widget.getLogs()),
                                        new BackgroundMail.OnSuccessCallback() {
                                            @Override
                                            public void onSuccess() {
                                                addLogAndUpdate(new Event("email sent", Event.TYPE.REGULAR));
                                            }
                                        },
                                        new BackgroundMail.OnFailCallback() {
                                            @Override
                                            public void onFail() {
                                                addLogAndUpdate(new Event("failed to send email", Event.TYPE.ERROR));
                                            }
                                        });
                                dialog.dismiss();
                                break;
                        }
                    }
                });
            }
        });
        dialog.show();

    }

    private void addLogAndUpdate(final Event event) {
        if (widget != null) {
            widget.setLog(event);
        }
        if (tvLogs != null) {
            tvLogs.setText(LogUtils.getLogsString(widget.getLogs()));
        }

    }

    private void clear() {
        if (tvLogs != null) {
            tvLogs.setText("");
        }
        if (widget != null) {
            widget.setLogs(new ArrayList<Event>());
        }
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST);
            return;
        }
    }

    private void onPermissionsNotGranted() {
        addLogAndUpdate(new Event(" overlay permission denied", Event.TYPE.ERROR));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                onPermissionsNotGranted();
            } else {
                addLogAndUpdate(new Event("overlay permission granted", Event.TYPE.ERROR));
                if (widget != null) {
                    widget.show();
                }
            }
        }
    }

    @Override
    public void onResponseSucces(TestResponse response) {
        addLogAndUpdate(new Event("Response success. Count : " + response.getCount(), Event.TYPE.ACTION));
    }

    @Override
    public void onResponseFailded() {
        addLogAndUpdate(new Event("Response failed.", Event.TYPE.ERROR));
    }
}
