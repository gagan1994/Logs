package com.tarams.loglibrary.asynctask;


import com.tarams.loglibrary.Logs;
import com.tarams.loglibrary.api.LogstatusEnum;
import com.tarams.loglibrary.db.LogDataBase;
import com.tarams.loglibrary.db.TraceAspectLogModelDb;

import java.util.List;

public class RestartAppTask extends AsyncTaskWrapper<Void, Void, String> {
    private final Logs logs;
    private final LogDataBase db;

    public RestartAppTask(Logs logs, AsyncHelper<Void, String> result) {
        super(result);
        this.logs = logs;
        db = logs.getLogDatabase();
    }

    @Override
    protected String doInBackground(Void... lists) {
        List<TraceAspectLogModelDb> logs = db.logList();
        if (logs == null || logs.isEmpty()) {
            return null;
        }
        for (TraceAspectLogModelDb item : logs) {
            item.status = LogstatusEnum.KILLED.status;
        }
        db.insertLogDatas(logs);
        return "Changed status of Logs size:" + logs.size();
    }
}
