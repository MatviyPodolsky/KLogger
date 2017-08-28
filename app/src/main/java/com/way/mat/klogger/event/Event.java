package com.way.mat.klogger.event;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: matviy;
 * Date: 8/28/17;
 * Time: 3:51 PM.
 */

public class Event {

    long time;
    String message;
    TYPE type;

    public enum TYPE {
        REGULAR,
        ACTION,
        ERROR
    }

    public Event(long time, String message, TYPE type) {
        this.time = time;
        this.message = message;
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(getTime())) + " : " + getMessage();
    }
}
