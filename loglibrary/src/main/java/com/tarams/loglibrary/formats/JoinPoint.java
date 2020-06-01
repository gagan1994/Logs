package com.tarams.loglibrary.formats;

public class JoinPoint {
    private  String fileName;
    private  int lineNumber;
    private  String methodName;

    public void init(String tag){
        int count=0;
        if(tag==null){
            count=1;
        }
        fileName=Thread.currentThread().getStackTrace()[count+6].getFileName();
        methodName=Thread.currentThread().getStackTrace()[count+6].getMethodName();
        lineNumber=Thread.currentThread().getStackTrace()[count+6].getLineNumber();
    }
    public String getFileName() {
        return fileName;
    }

    public int getLine() {
        return lineNumber;
    }

    public String getMethodName() {
        return methodName;
    }

}
