package com.way.mat.klogger.util;

import android.content.Context;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

/**
 * Author: matviy;
 * Date: 8/28/17;
 * Time: 4:42 PM.
 */

public class EmailUtils {

    public static void sendEmail(Context context, String to, String message,
                                 BackgroundMail.OnSuccessCallback successCallback,
                                 BackgroundMail.OnFailCallback failCallback) {
        BackgroundMail.newBuilder(context)
                .withUsername("matviy.podolskiy@gmail.com")
                .withPassword("asdsasds")
                .withMailto(to)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Logs")
                .withBody(message)
                .withOnSuccessCallback(successCallback)
                .withOnFailCallback(failCallback)
                .withProcessVisibility(true)
                .send();
    }

}
