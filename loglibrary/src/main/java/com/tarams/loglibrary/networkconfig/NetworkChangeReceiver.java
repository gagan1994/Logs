package com.tarams.loglibrary.networkconfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.HashSet;

import static com.tarams.loglibrary.Logs.hasInternetConnectionM;
import static com.tarams.loglibrary.Logs.info;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final long DELAY_TO_CHECK = 6000;
    private final HashSet<NetworkStateReceiverListener> listeners;
    private Boolean connected;
    boolean isBusy;

    public NetworkChangeReceiver() {
        listeners = new HashSet<>();
    }

    public void onReceive(final Context context, Intent intent) {
        if (!isInitialStickyBroadcast()) {
            if (!isBusy) {
                isBusy = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBusy = false;
                        connected = hasInternetConnectionM(context);
                        if (connected) {
                            log("Network connected");
                        } else {
                            log("Network not connected");
                        }
                        notifyStateToAll();
                    }
                }, DELAY_TO_CHECK);
            }
        }
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkStateReceiverListener l) {
        listeners.remove(l);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if (connected == null || listener == null)
            return;
        listener.onNetworkChange(connected);
    }

    private void log(String message) {
        info("NetworkChangeReceiver "+message + this);
    }
}
