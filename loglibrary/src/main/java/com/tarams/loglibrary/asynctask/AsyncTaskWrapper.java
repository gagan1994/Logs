package com.tarams.loglibrary.asynctask;

import android.os.AsyncTask;

import java.util.UUID;

public abstract class AsyncTaskWrapper<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private final AsyncHelper<Progress,Result> result;
    protected final String reqId;

    public AsyncTaskWrapper(AsyncHelper<Progress,Result> result){
        reqId= UUID.randomUUID()+"";
        this.result=result;
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        result.onProgressChanged(values);
    }

    @Override
    protected void onPostExecute(Result v) {
        result.success(v);
    }
}
