package com.tarams.loglibrary.formats;

public enum LogEnum {
    VERBOSE(2,"VERBOSE"),
    DEBUG(3,"DEBUG"),
    INFO(4,"INFO"),
    WARN(5,"WARN"),
    ERROR(6,"ERROR"),
    NONE(-1,"NONE"),
    ALL(0,"ALL");
    private final int logType;
    private final String log;
    LogEnum(int type,String log){
        this.log=log;
        this.logType=type;
    }

    public int getLogType() {
        return logType;
    }

    public String getLog() {
        return log;
    }

}