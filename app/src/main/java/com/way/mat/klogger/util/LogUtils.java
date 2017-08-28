package com.way.mat.klogger.util;

import com.way.mat.klogger.event.Event;

import java.util.List;

/**
 * Author: matviy;
 * Date: 8/28/17;
 * Time: 12:13 PM.
 */

public class LogUtils {

    public static String getLogsString(List<Event> events) {
        StringBuilder sbText = new StringBuilder();
        if (events != null) {
            for (final Event event :
                    events) {
                sbText.append(event.toString()).append("\n");
            }
        }
        return sbText.toString();
    }

}
