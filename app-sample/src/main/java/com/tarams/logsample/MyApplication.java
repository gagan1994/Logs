package com.tarams.logsample;

import android.app.Application;

import com.tarams.loglibrary.LogWrapper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogWrapper.initilize(this,"YWRtaW46YWRtaW4=");
    }
}
