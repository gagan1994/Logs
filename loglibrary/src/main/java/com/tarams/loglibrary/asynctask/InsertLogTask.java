package com.tarams.loglibrary.asynctask;

import com.tarams.loglibrary.Logs;
import com.tarams.loglibrary.api.LogstatusEnum;
import com.tarams.loglibrary.db.LogDataBase;
import com.tarams.loglibrary.db.TraceAspectLogModelDb;
import com.tarams.loglibrary.formats.JoinPoint;

import java.util.Calendar;

import static com.tarams.loglibrary.Logs.info;


public class InsertLogTask extends AsyncTaskWrapper<String, Void, String> {
    private final LogDataBase db;
    private final Logs wrapper;
    private final JoinPoint joinPoint;

    public InsertLogTask(Logs wrapper, JoinPoint joinPoint, AsyncHelper<Void, String> result) {
        super(result);
        this.joinPoint = joinPoint;
        this.db = wrapper.getLogDatabase();
        this.wrapper = wrapper;
    }

    @Override
    protected String doInBackground(String... lists) {
        String item = lists[0];
        info("insertion started: " + item);
        TraceAspectLogModelDb model = new TraceAspectLogModelDb();
        model.message = item;
        model.timestamp = Calendar.getInstance().getTimeInMillis();
        model.logLevel = lists[1];

        model.className = joinPoint.getFileName();
        model.lineNum = joinPoint.getLine();
        model.methodName = joinPoint.getMethodName();

        if (!wrapper.isConnected()) {
            model.status = LogstatusEnum.FAILED.status;
        }
        db.insertLogData(model);
        if (model.status == LogstatusEnum.FAILED.status) {
            info("changed logs status to failed");
            return null;
        }
        info("insertion completed");
        return "success " + model.message;
    }

}
