package com.example.yandexstationshare;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MyWebViewClient extends WebViewClient {

    final private static String TAG = "Main";

    private static String token;
    private static String session;
    private EventAfterRegister afterRegister;

    public MyWebViewClient(EventAfterRegister callback) {
        this.afterRegister = callback;
    }

    protected boolean iSeeViewoPage = false;

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
        return true;
    }

    protected static class Callback implements ValueCallback<String> {
        private WebView view;
        private EventAfterRegister afterRegister;

        public Callback(WebView view, EventAfterRegister afterRegister) {
            this.view = view;
            this.afterRegister = afterRegister;
        }

        public void onReceiveValue(String json) {
            try {
                view.stopLoading();
                view.destroy();

                JSONObject object = new JSONObject(json);
                JSONObject content = object.getJSONObject("quasar-controller");

                Log.d("Main", content.getString("token"));
                token = content.getString("token");
                Log.d("Main", "Your Auth:" + session);

                //@TODO replace device id
                afterRegister.register(new YandexUser(token, session));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        String cookies = CookieManager.getInstance().getCookie(url);
        session = getCookie("Session_id", cookies);
        if (session == null) {
            return;
        }

        Log.d(TAG, url);
        if (!iSeeViewoPage) {
            iSeeViewoPage = true;
            view.evaluateJavascript(
                    "(function(){return JSON.parse(document.getElementsByClassName('quasar-controller_with-devices')[0].getAttribute('data-bem'));})();",
                    new Callback(view, this.afterRegister)
            );
        }
    }

    protected String getCookie(String cookieName, String cookies) {
        String CookieValue = null;

        Log.d("Main", "Cookies :" + cookies);
        String[] temp = cookies.split(";");
        for (String ar1 : temp) {
            ar1 = ar1.trim();
            if (ar1.contains(cookieName)) {
                String[] temp1 = ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }

}
