package com.example.yandexstationshare.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.yandexstationshare.logger.DefautLogger;
import com.example.yandexstationshare.logger.Logger;
import com.example.yandexstationshare.logger.NullLogger;
import com.example.yandexstationshare.ui.webview.MyWebViewClient;
import com.example.yandexstationshare.api.models.YandexUser;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.yandexstationshare.api.Api;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class WebActivity extends AppCompatActivity {

    final private static String TAG = "Main";

    final private static Logger logger = new DefautLogger();
    final private static Api api = new Api(logger);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //CookieManager.getInstance().flush();

        final WebView myWebView = new WebView(this);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient((YandexUser user) -> {
            api.authorization(user);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Авторизован",
                    Toast.LENGTH_SHORT
            );
            toast.show();

            if (send()) {
                finish();
            }
        }));

        myWebView.loadUrl("https://passport.yandex.ru/auth/welcome?backpath=https://yandex.ru&origin=home_touch_new");
        setContentView(myWebView);
    }

    protected boolean send() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendURL(intent);
            return true;
        }
        return false;
    }


    void handleSendURL(Intent intent) {
        try {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.d(TAG, "YOUTUBE > " + sharedText);

            if (sharedText != null) {
                URL url = new URL(sharedText);
                api.send(url);
                Toast toast = Toast.makeText(getApplicationContext(),
                        sharedText,
                        Toast.LENGTH_SHORT
                );
                toast.show();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
