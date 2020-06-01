package com.tarams.loglibrary.asynctask;

public abstract class AsyncHelper<Progress,Result> {
    public abstract void success(Result obj);

    public void error(Result error){
        /*
         * Override for error
         * */
    }

    public void onProgressChanged(Progress... progress) {
        /*
         * Override for progress
         * */
    }
}
