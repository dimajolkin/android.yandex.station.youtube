package com.example.yandexstationshare;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {


    @Override
    public void onPageFinished(WebView view, String url) {
        Log.v("Main", "FINISH PAHE LOAD");
        String cookies = CookieManager.getInstance().getCookie(url);

        Log.v("Main", "All the cookies in a string:" + cookies);

    }
}
