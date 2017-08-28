package com.way.mat.klogger.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.way.mat.klogger.R;
import com.way.mat.klogger.event.Event;
import com.way.mat.klogger.ui.widget.LoggerWidget;
import com.way.mat.klogger.util.EmailUtils;
import com.way.mat.klogger.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final String TAG = "KLogger";

    public static final int OVERLAY_PERMISSION_REQUEST = 10001;

    @BindView(R.id.logs)
    TextView tvLogs;

    private List<Event> mEvents = new ArrayList<>();
    private LoggerWidget widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        widget = new LoggerWidget.Builder()
                .setContext(this)
                .build();
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.action_refresh)
    public void writeLogs() {
        mEvents.add(new Event(System.currentTimeMillis(), "write logs clicked", Event.TYPE.REGULAR));
        tvLogs.setText(LogUtils.getLogsString(mEvents));
        widget.setLogs(mEvents);
    }

    @OnClick(R.id.action_toggle_overlay)
    public void toggleOverlay() {
        mEvents.add(new Event(System.currentTimeMillis(), "toggle overlay clicked", Event.TYPE.REGULAR));
        tvLogs.setText(LogUtils.getLogsString(mEvents));
        if (widget.isShown()) {
            widget.hide();
        } else {
            if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))) {
                widget.show();
            } else {
                Log.d(TAG, "Can't change state! Device does not have drawOverlays permissions!");
                checkOverlayPermission();
            }
        }
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                onPermissionsNotGranted();
            } else {
                if (widget != null) {
                    widget.show();
                }
            }
        }
    }

    private void onPermissionsNotGranted() {
        mEvents.add(new Event(System.currentTimeMillis(), " overlay permission denied", Event.TYPE.ERROR));
        tvLogs.setText(LogUtils.getLogsString(mEvents));
    }

    @OnClick(R.id.action_email)
    public void sendEmail() {
        mEvents.add(new Event(System.currentTimeMillis(), "send logs clicked", Event.TYPE.ACTION));
        View view = LayoutInflater.from(this).inflate(
                R.layout.layout_dialog, null);
        final AppCompatEditText etEmail = view.findViewById(R.id.et_email);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                        final String email = etEmail.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            mEvents.add(new Event(System.currentTimeMillis(), "empty email", Event.TYPE.ERROR));
                            etEmail.setError("Please, enter email");
                            etEmail.requestFocus();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            mEvents.add(new Event(System.currentTimeMillis(), "invalid email", Event.TYPE.ERROR));
                            etEmail.setError("Please, enter correct email");
                            etEmail.requestFocus();
                        } else {
                            mEvents.add(new Event(System.currentTimeMillis(), "sending email...", Event.TYPE.REGULAR));
                            EmailUtils.sendEmail(MainActivity.this, email, LogUtils.getLogsString(mEvents),
                                    new BackgroundMail.OnSuccessCallback() {
                                        @Override
                                        public void onSuccess() {
                                            mEvents.add(new Event(System.currentTimeMillis(), "email sent", Event.TYPE.REGULAR));
                                        }
                                    },
                                    new BackgroundMail.OnFailCallback() {
                                        @Override
                                        public void onFail() {
                                            mEvents.add(new Event(System.currentTimeMillis(), "failed to send email", Event.TYPE.ERROR));
                                        }
                                    });
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();

    }

}
