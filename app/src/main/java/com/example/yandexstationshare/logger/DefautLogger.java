package com.example.yandexstationshare.logger;

import android.util.Log;

public class DefautLogger implements Logger {

    final private static String TAG = "Main";

    public void log(String message) {
        Log.e(TAG, message);
    }

    @Override
    public void exception(Exception ex) {
        Log.e(TAG, ex.getMessage(), ex);
    }
}
