package com.way.mat.klogger.ui.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Author: matviy;
 * Date: 8/29/17;
 * Time: 10:55 AM.
 */

public class CustomTextWatcher implements TextWatcher {

    private EditText mEditText;

    public CustomTextWatcher(EditText et) {
        mEditText = et;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mEditText.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
