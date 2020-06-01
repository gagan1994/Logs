package com.tarams.loglibrary.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.tarams.loglibrary.Utils;
import com.tarams.loglibrary.db.TraceAspectLogModelDb;

public class PostModel {
    private final String status;
    private final String logLevelName;
    private final String className;
    private final int lineNum;
    private final String message;
    private final String methodName;
    private final String timeStamp;
    private final String packageName;
    private String versionName;
    private final String deviceId;
    private final int osVersion;
    private final String deviceType;

    public PostModel(Context context, TraceAspectLogModelDb traceAspectLogModelDb) {
        status = getStatus(traceAspectLogModelDb.status);
        logLevelName = traceAspectLogModelDb.logLevel;
        className = traceAspectLogModelDb.className;
        lineNum = traceAspectLogModelDb.lineNum;
        message = traceAspectLogModelDb.message;
        methodName = traceAspectLogModelDb.methodName;
        timeStamp = Utils.getUTCtime();
        packageName = context.getPackageName();
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        osVersion = Build.VERSION.SDK_INT;
        deviceType="Android";
    }

    private String getStatus(int status) {
        return LogstatusEnum.get(status).statusString;
    }
}
