package com.tarams.loglibrary;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tarams.loglibrary.asynctask.AsyncHelper;
import com.tarams.loglibrary.asynctask.Delete;
import com.tarams.loglibrary.asynctask.InsertLogTask;
import com.tarams.loglibrary.asynctask.RestartAppTask;
import com.tarams.loglibrary.asynctask.UpdateServer;
import com.tarams.loglibrary.db.LogDataBase;
import com.tarams.loglibrary.formats.JoinPoint;
import com.tarams.loglibrary.formats.LogEnum;
import com.tarams.loglibrary.formats.LogFormat;
import com.tarams.loglibrary.networkconfig.NetworkChangeReceiver;
import com.tarams.loglibrary.networkconfig.NetworkStateReceiverListener;


import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;

import static com.tarams.loglibrary.LogWrapper.DEFAULT_DB_NAME;

public final class Logs implements NetworkStateReceiverListener {
    private static final int DEFAULT_MESSAGE_LENGTH = 100;
    private static final String TAG = "LogWrapper";
    private final String dbName;
    private final LogDataBase logDatabase;
    private final Context context;
    protected JoinPoint jointPoint;
    protected LogFormat logFormat;
    protected int messageLength;

    private static final int MESSAGE_SEND_LOGS = 1;
    private static final long DEFAULT_DELAY = 1000;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateToServer();
        }
    };
    private Set<LogEnum> logEnumset;
    private final String token;
    private NetworkChangeReceiver networkChangeReceiver;
    private static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    public boolean isConnected() {
        return hasInternetConnectionM(context);
    }

    public static boolean hasInternetConnectionM(final Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            final ConnectivityManager connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            final Network network = connectivityManager.getActiveNetwork();
            final NetworkCapabilities capabilities = connectivityManager
                    .getNetworkCapabilities(network);
            return capabilities != null
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } else {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }
    }

    protected Logs(LogWrapper logWrapper,Realm realm) {
        jointPoint = new JoinPoint();
        if (logWrapper.getContext() == null) {
            throw new NullPointerException("Context cannot be null");
        }
        if(realm==null){
            Realm.init(logWrapper.getContext());
        }
        token = logWrapper.getToken();
        if (token == null) {
            throw new NullPointerException("Please provide token, token can be null");
        }
        context = logWrapper.getContext();
        messageLength = DEFAULT_MESSAGE_LENGTH;
        dbName = logWrapper.getDbName() == null ? DEFAULT_DB_NAME : logWrapper.getDbName();
        logDatabase = new LogDataBase();
        logFormat=new LogFormat(logWrapper.getContext());
        initApp();
    }

    public static void info(String m) {
        Log.i(TAG, m);
    }

    public void logData(LogEnum logEnum, String tag, String message) {
        if (logEnumset == null || logEnumset.contains(logEnum)) {
            checkConstraints(message);
            String logMessage = logFormat.formatLogMessage(context, logEnum, tag, message);
            JoinPoint joinPoint = getJointPoint(tag);
            insertMessage(logMessage, logEnum, joinPoint);
        } else {
            info("didn't save tag: " + tag + " message: " + message);
        }
    }

    private JoinPoint getJointPoint(String tag) {
        jointPoint.init(tag);
        return jointPoint;
    }


    private void checkConstraints(String message) {
        if (message == null) {
            throw new NullPointerException("Message cannot be null");
        }
        if (message.length() > messageLength) {
            throw new IllegalStateException("Message cannot  be more than " + messageLength +
                    " Length, Check Configuration for more");
        }
    }

    public LogDataBase getLogDatabase() {
        return logDatabase;
    }

    private void insertMessage(String logMessage, LogEnum logEnum, JoinPoint joinPoint) {
        new InsertLogTask(this, joinPoint, new AsyncHelper<Void, String>() {
            @Override
            public void success(String obj) {
                ;
                if (obj == null) return;
                info(obj);
                mHandler.removeMessages(MESSAGE_SEND_LOGS);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_SEND_LOGS, "send"), DEFAULT_DELAY);
            }
        }).execute(logMessage, logEnum.getLog());
    }

    public void syncUpServer() {
        Log.i("SyncUp", "---Sync up---");
    }

    private void updateToServer() {
        info("Update to server");
        new UpdateServer(new AsyncHelper<Void, String[]>() {
            @Override
            public void success(final String[] ids) {
                if (ids == null) {
                    info("Error uploading logs");
                } else {
                    new Delete(new AsyncHelper<Void, String>() {
                        @Override
                        public void success(String obj) {
                            info("Delted all objects" + ids);
                        }
                    }, Logs.this)
                            .execute(ids);
                }
            }
        }, this).execute();
    }

    private void initApp() {
        new RestartAppTask(this, new AsyncHelper<Void, String>() {
            @Override
            public void success(String s) {
                if (s != null) {
                    info("updated logs when app Restarted size:" + s);
                    mHandler.removeMessages(MESSAGE_SEND_LOGS);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_SEND_LOGS, "send"), DEFAULT_DELAY);
                } else {
                    info("Log Db list since empty");
                }
            }
        }).execute();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if (isConnected) {
            syncUpServer();
        }
    }

    public void connectListener(Activity activity) {
        info("connectListener from MyLocationListener");
        IntentFilter intentFilter = new IntentFilter(CONNECTIVITY_CHANGE);
        networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.addListener(new NetworkStateReceiverListener() {
            @Override
            public void onNetworkChange(boolean isConnected) {

            }
        });
        activity.registerReceiver(networkChangeReceiver, intentFilter);
    }

    public void disconnectListener(Activity activity) {
        info("disconnectListener from MyLocationListener");
        if (networkChangeReceiver != null) {
            activity.unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    public String getToken() {
        return token;
    }

    public void setLogEnums(LogEnum[] logEnums) {
        if (logEnums != null) {
            logEnumset = new HashSet<>();
            for (int i = 0; i < logEnums.length; i++) {
                if (logEnums[i] == LogEnum.ALL) {
                    logEnumset = null;
                    break;
                }
                if (logEnums[i] == LogEnum.NONE) {
                    logEnumset.clear();
                    break;
                }
                logEnumset.add(logEnums[i]);
            }
        } else {
            logEnumset = null;
        }
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
        if (messageLength < DEFAULT_MESSAGE_LENGTH) {
            throw new IllegalStateException("Message Length cannot  be less than " + DEFAULT_MESSAGE_LENGTH);
        }
    }
}
