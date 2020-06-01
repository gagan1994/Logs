package com.tarams.loglibrary.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TraceAspectLogModelDb extends RealmObject {
    @PrimaryKey
    public String id;
    public String message;
    public String logLevel;
    public Long timestamp;
    public int status;
    public String className;
    public int lineNum;
    public String methodName;

    public TraceAspectLogModelDb() {
    }

    public TraceAspectLogModelDb(TraceAspectLogModelDb item) {
        id = item.id;
        message = item.message;
        logLevel = item.logLevel;
        timestamp = item.timestamp;
        status = item.status;
        className = item.className;
        lineNum = item.lineNum;
        methodName = item.methodName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public TraceAspectLogModelDb withMessage(String message) {
        setMessage(message);
        return this;
    }

    public TraceAspectLogModelDb withTimeMills(Long timeMills) {
        setTimestamp(timeMills);
        return this;
    }
}
