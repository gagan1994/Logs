package com.tarams.logsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tarams.loglibrary.LogWrapper;
import com.tarams.loglibrary.Logs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Logs logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.clickMeBtn).setOnClickListener(this);
        LogWrapper.connectListener(this);
    }

    @Override
    public void onClick(View v) {
        LogWrapper.logD("logD  log example");
        LogWrapper.logD("MainActivity", "logD  log example with tag");


        LogWrapper.logW("logW  log example");
        LogWrapper.logW("MainActivity", "logW  log example with tag");


        LogWrapper.logE("logE  log example");
        LogWrapper.logE("MainActivity", "logE  log example with tag");


        LogWrapper.logV("logV  log example");
        LogWrapper.logV("MainActivity", "logV  log example with tag");

        LogWrapper.logI("logI  log example");
        LogWrapper.logI("MainActivity", "logI  log example with tag");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogWrapper.disconnectListener(this);
    }
}
