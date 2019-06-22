package com.example.yandexstationshare.ui;

import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.yandexstationshare.R;
import com.example.yandexstationshare.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 *
 */
public class WebActivity extends AppCompatActivity {

    private static String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WebView myWebView = new WebView(this);
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            protected boolean iSeeViewoPage = false;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                return true;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {

                URL currentURL = null;
                try {
                    currentURL = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String cookies = CookieManager.getInstance().getCookie(url);
                final String session = getCookie("Session_id", cookies);
                if (session == null) {
                    return;
                }

                Log.e(TAG, url);
                if (!iSeeViewoPage) {
                    iSeeViewoPage = true;
                    view.evaluateJavascript("(function(){return JSON.parse(document.getElementsByClassName('quasar-controller_with-devices')[0].getAttribute('data-bem'));})();",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String json) {
                                    try {
                                        view.stopLoading();
                                        view.destroy();

                                        JSONObject object = new JSONObject(json);
                                        JSONObject content = object.getJSONObject("quasar-controller");

                                        Log.e("Main", content.getString("token"));
                                        nextActivity(session, content.getString("token"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            }

            public String getCookie(String cookieName, String cookies) {
                String CookieValue = null;

                Log.e("Main", "Cookies :" + cookies);
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
        });

        myWebView.loadUrl("https://yandex.ru/video/touch/search?text=youtube");
        setContentView(myWebView);
    }

    protected void nextActivity(String session, String token) {
        Log.e("Main", "Your Auth:" + session);
        Intent intent = new Intent(WebActivity.this, MainActivity.class);
        intent.putExtra("session_id", session);
        intent.putExtra("token", token);
        startActivity(intent);
        finish();
    }
}
