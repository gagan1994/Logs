package com.tarams.loglibrary.formats;

import android.content.Context;
import android.provider.Settings;

import com.tarams.loglibrary.Utils;

public class LogFormat {

    private String deviceUUID;

    public LogFormat(Context context) {
        Context mContext = context.getApplicationContext();
        deviceUUID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Implement this method to override the default log message format.
     *
     * @param context
     * @param logLevel The level of logcat logging that Parse should do.
     * @param message  Log message that need to be customized.
     * @return Formatted Log Message that will store in database.
     */
    public String formatLogMessage(Context context, LogEnum logLevel, String tag, String message) {
        String timeStamp = Utils.getCurrentTime();
        String packageName = "Android_" + context.getPackageName();
        String logLevelName = logLevel.getLog();
        return tag != null ?
                getFormattedLogMessage(logLevelName, tag, message, timeStamp, packageName, deviceUUID) :
                getFormattedLogMessage(logLevelName, message, timeStamp, packageName, deviceUUID)
                ;
    }

    public String getFormattedLogMessage(String logLevelName, String tag, String message, String timeStamp,
                                         String packageName, String deviceUUID) {

        return timeStamp + " | " + deviceUUID + " | " + "[" + logLevelName + "/" + tag + "]: " + message;
    }

    public String getFormattedLogMessage(String logLevelName, String message, String timeStamp,
                                         String packageName, String deviceUUID) {

        return timeStamp + " | " + deviceUUID + " | " + "[" + logLevelName + "]: " + message;
    }

}
