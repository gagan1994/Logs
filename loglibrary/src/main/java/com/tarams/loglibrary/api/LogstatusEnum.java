package com.tarams.loglibrary.api;

public enum LogstatusEnum {
    FAILED(1,"Failed"),
    KILLED(2,"Killed"),
    SUCCESS(0,"Success");
    public final int status;
    public final String statusString;

    LogstatusEnum(int status, String statusString) {
        this.status = status;
        this.statusString = statusString;
    }

    public static LogstatusEnum get(int status) {
        switch (status){
            case 1:return FAILED;
            case 2:return KILLED;
        }
        return SUCCESS;
    }
}
