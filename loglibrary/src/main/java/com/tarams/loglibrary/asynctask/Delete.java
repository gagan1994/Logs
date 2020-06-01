package com.tarams.loglibrary.asynctask;


import com.tarams.loglibrary.Logs;

import static com.tarams.loglibrary.Logs.info;

public class Delete extends AsyncTaskWrapper<String, Void, String> {
    private final Logs logs;

    public Delete(AsyncHelper<Void, String> result, Logs logs) {
        super(result);
        this.logs = logs;
    }

    @Override
    protected String doInBackground(String... strings) {
        info("Upload logs Started deleting " + strings);
        logs.getLogDatabase().deleteLogs(strings);
        return "Success";
    }
}
