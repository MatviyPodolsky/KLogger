package com.way.mat.klogger.view;

import com.way.mat.klogger.newtwork.response.TestResponse;

/**
 * Author: matviy;
 * Date: 8/14/17;
 * Time: 3:53 PM.
 */

public interface MainView extends BaseView {

    void onResponseSucces(TestResponse response);

    void onResponseFailded();

}
