package com.example.yandexstationshare.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.yandexstationshare.EventAfterRegister;
import com.example.yandexstationshare.MyWebViewClient;
import com.example.yandexstationshare.YandexStation;
import com.example.yandexstationshare.YandexStationApi;
import com.example.yandexstationshare.YandexUser;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class WebActivity extends AppCompatActivity {

    final private static String TAG = "Main";



    public static final YandexStationApi yandexStationApi = new YandexStationApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WebView myWebView = new WebView(this);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient(new EventAfterRegister() {
            public void register(YandexUser user) {


                yandexStationApi.init(user);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Авторизован",
                        Toast.LENGTH_SHORT
                );
                toast.show();

                send();
                finish();
            }
        }));

        myWebView.loadUrl(yandexStationApi.getVideoURL());
        setContentView(myWebView);
    }

    protected void send() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendURL(intent);
        }
    }


    void handleSendURL(Intent intent) {
        try {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.d(TAG, "YOUTUBE > " + sharedText);

            if (sharedText != null) {
                URL url = new URL(sharedText);
                yandexStationApi.send(url);
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
