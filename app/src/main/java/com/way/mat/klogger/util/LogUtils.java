package com.way.mat.klogger.util;

import com.way.mat.klogger.event.Event;

import java.util.ArrayList;
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

    public static List<Event> getFilteredList(List<Event> logs, Event.TYPE filter) {
        List<Event> filteredList = new ArrayList<>();
        if (logs == null) {
            return filteredList;
        }
        if (filter == null) {
            return new ArrayList<>(logs);
        }
        for (final Event event :
                logs) {
            if (event.getType() == filter) {
                filteredList.add(event);
            }
        }
        return filteredList;
    }

}
