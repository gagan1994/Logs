# Remote Logs

* [Overview](#overview)
* [Log format](#log-format)
* [Download](#download)
* [Initialize](#initialize)
* [Usage](#usage)
* [Custom Log Message Format](#custom-log-message-format)
* [Additional Methods](#additional-methods)
* [License](https://github.com/gagan1994/AwsLogWrapper/blob/master/LICENSE)

## Overview
Remote Log is a library for Android on top of standard Android `Log` class for debugging purpose.
This is a simple library that will allow Android apps or library to push logs to Remote log Server, it internally stores all logs 
in local Db(Realm) and it pushes batch of logs to a stream` for `debugging` purpose. Want to know more on this and wondering why you should prefer using this library over doing it yourself.


## Log Format
```
timeStamp + " | " + osVersion + " | " + deviceUUID + " | [" + logLevelName + "]: " + message
```
```
2017-10-05T14:46:36.541Z 1.0 | Android-7.1.1 | 62bb1162466c3eed | [INFO]: Log message
```

## Download
Download the latest version or grab via Gradle.

The library is available on [`jcenter()`](http://jcenter.bintray.com/com/hypertrack/hyperlog/). In your module's `build.gradle`, add the following code snippet and run the `gradle-sync`.

```
allprojects {
      ...
      repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}   
```

Add below to app's `build.gradle` `latest_version=1.0.2`

```

dependencies {
    ...
	  implementation 'com.github.gagan1994:Logs:$latest_version'
    ...
}
```

## Initialize
Inside `onCreate` of Application class or Launcher Activity.

        LogWrapper.initilize(this,"{TOKEN}");

```
## Usage
```
String message = "Log message";
awsWrapper.logI(message);
                
```
## Custom Log Message Format
Default Log Message that will store in database.
```
timeStamp + " | " + osVersion + " | " + deviceUUID + " | [" + logLevelName + "]: " + message
```
```
2017-10-05T14:46:36.541Z 1.0 | 0.0.1 : Android-7.1.1 | 62bb1162466c3eed | [INFO]: Log message
```
This message can easily be customize.
1. Create a new class extending `LogFormat`.
2. Override `getFormattedLogMessage` method.
3. Now return the formatted message.
```
class CustomLogMessageFormat extends LogFormat {

    CustomLog(Context context) {
        super(context);
    }

    public String getFormattedLogMessage(String logLevelName, String message, String timeStamp,
                                         String senderName, String osVersion, String deviceUUID) {
                                         
        //Custom Log Message Format                                
        String formattedMessage = timeStamp + " : " + logLevelName + " : " + deviceUUID + " : " + message;
        
        return formattedMessage;
    }
}

```
Custom Log Message Format example
```
2017-10-05T14:46:36.541Z 1.0 | INFO | 62bb1162466c3eed | Log has been pushed

```
4. Above created class instance then needs to be passed while initializing `AwsWrapper` or can be set later.
```
        LogWrapper.withLogForamat(new CustomLogMessageFormat(this));
        
```
## Additional Methods
* Different types of log.
```
LogWrapper.logD(TAG,"debug");
LogWrapper.logI(information");
LogWrapper.logE("error");
LogWrapper.logV("verbose");
LogWrapper.logW("warning");
```




