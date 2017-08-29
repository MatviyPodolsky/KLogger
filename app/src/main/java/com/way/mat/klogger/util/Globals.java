package com.way.mat.klogger.util;

/**
 * Author: matviy;
 * Date: 8/29/17;
 * Time: 11:21 AM.
 */

public interface Globals {

    int MINIMUM_TEXT_SIZE = 8;
    int DEFAULT_TEXT_SIZE = 12;

    enum FILTER {
        REGULAR,
        ACTION,
        ERROR,
        ALL
    }
}
