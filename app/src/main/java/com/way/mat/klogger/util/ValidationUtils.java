package com.way.mat.klogger.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Author: matviy;
 * Date: 8/29/17;
 * Time: 10:58 AM.
 */

public class ValidationUtils {

    public enum STATUS {
        EMPTY_EMAIL,
        INVALID_EMAIL,
        SUCCESS
    }

    public static STATUS validateEmail(final EditText etEmail) {
        if (etEmail == null) {
            return STATUS.EMPTY_EMAIL;
        }
        final String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please, enter email");
            etEmail.requestFocus();
            return STATUS.EMPTY_EMAIL;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please, enter correct email");
            etEmail.requestFocus();
            return STATUS.INVALID_EMAIL;
        } else {
            return STATUS.SUCCESS;
        }
    }

}
