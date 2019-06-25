package com.example.yandexstationshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yandexstationshare.R;
import com.example.yandexstationshare.api.Api;
import com.example.yandexstationshare.api.models.YandexUser;
import com.example.yandexstationshare.logger.DefautLogger;
import com.example.yandexstationshare.logger.Logger;
import com.example.yandexstationshare.ui.webview.MyWebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class WebActivity extends AppCompatActivity {

    final private static String TAG = "Main";

    final private static Logger logger = new DefautLogger();
    final private static Api api = new Api(logger);

    private void onAuth() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            myWebView.setVisibility(View.GONE);
            setContentView(R.layout.activity_web);
            TextView text = (TextView) findViewById(R.id.textView);
            TextView text1 = (TextView) findViewById(R.id.textView2);

            text.setText("Token: " + user.getToken());
            text1.setText("Session: " + user.getSession());


        }));

        myWebView.loadUrl("https://passport.yandex.ru/auth/welcome");
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
