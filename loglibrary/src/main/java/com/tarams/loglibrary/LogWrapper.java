package com.tarams.loglibrary;

import android.app.Activity;
import android.content.Context;

import com.tarams.loglibrary.formats.JoinPoint;
import com.tarams.loglibrary.formats.LogEnum;
import com.tarams.loglibrary.formats.LogFormat;

import io.realm.Realm;


public class LogWrapper {
    private Context context;
    protected static Logs logsInstance;

    private String dbName;
    protected static final String DEFAULT_DB_NAME = "LOG_DB";
    private String token;

    public static void initilize(Context application, String s) {
        LogWrapper wrapper = new LogWrapper()
                .withContext(application)
                .withToken(s);
        logsInstance = new Logs(wrapper,null);
    }

    public static void initilize(Context application, String token, String dbName) {
        LogWrapper wrapper = new LogWrapper()
                .withContext(application)
                .withToken(token)
                .withDbName(dbName);
        logsInstance = new Logs(wrapper,null);
    }

    public static void initilize(Context application, String token, String dbName, JoinPoint joinPoint) {
        LogWrapper wrapper = new LogWrapper()
                .withContext(application)
                .withToken(token)
                .withDbName(dbName);
        logsInstance = new Logs(wrapper,null);
        logsInstance.jointPoint = joinPoint;
    }
    public static void initilize(Context application, Realm realm, String token) {
        LogWrapper wrapper = new LogWrapper()
                .withContext(application)
                .withToken(token);
        logsInstance = new Logs(wrapper,realm);
    }

    public static void initilize(Context application, Realm realm, String token, String dbName) {
        LogWrapper wrapper = new LogWrapper()
                .withContext(application)
                .withToken(token)
                .withDbName(dbName);
        logsInstance = new Logs(wrapper,realm);
    }

    public static void initilize(Context application, Realm realm, String token, String dbName, JoinPoint joinPoint) {
        LogWrapper wrapper = new LogWrapper()
                .withContext(application)
                .withToken(token)
                .withDbName(dbName);
        logsInstance = new Logs(wrapper,realm);
        logsInstance.jointPoint = joinPoint;
    }

    public static void connectListener(Activity activity) {
        checkConstraints();
        logsInstance.connectListener(activity);
    }

    public static void disconnectListener(Activity activity) {
        checkConstraints();
        logsInstance.disconnectListener(activity);
    }

    private LogWrapper withDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    private LogWrapper withContext(Context context) {
        this.context = context;
        return this;
    }

    public static void withMessageLength(int messageLength) {
        checkConstraints();
        logsInstance.setMessageLength(messageLength);
    }

    private static void checkConstraints() {
        if (logsInstance == null)
            throw new NullPointerException("Initilize SDK first");
    }

    public void withLogForamt(LogFormat logFormat) {
        checkConstraints();
        logsInstance.logFormat = logFormat;
    }

    public static void logonly(LogEnum... logEnums) {
        checkConstraints();
        logsInstance.setLogEnums(logEnums);
    }

    protected Context getContext() {
        return context;
    }


    protected String getDbName() {
        return dbName;
    }

    protected String getToken() {
        return token;
    }

    protected LogWrapper withToken(String token) {
        this.token = token;
        return this;
    }

    public static void logI(String tag, String message) {
        checkConstraints();
        logsInstance.logData(LogEnum.INFO, tag, message);
    }

    public static void logW(String tag, String message) {
        checkConstraints();
        logsInstance.logData(LogEnum.WARN, tag, message);
    }

    public static void logV(String tag, String message) {
        checkConstraints();
        logsInstance.logData(LogEnum.VERBOSE, tag, message);
    }

    public static void logE(String tag, String message) {
        checkConstraints();
        logsInstance.logData(LogEnum.ERROR, tag, message);
    }

    public static void logD(String tag, String message) {
        checkConstraints();
        logsInstance.logData(LogEnum.DEBUG, tag, message);
    }


    public static void logI(String message) {
        logI(null, message);
    }

    public static void logW(String message) {
        logW(null, message);
    }

    public static void logV(String message) {
        logV(null, message);
    }

    public static void logE(String message) {
        logE(null, message);
    }

    public static void logD(String message) {
        logD(null, message);
    }
}
