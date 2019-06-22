package com.example.yandexstationshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yandexstationshare.R;
import com.example.yandexstationshare.YandexStationApi;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    final private static String TAG = "Main";

    public static final YandexStationApi yandexStationApi = new YandexStationApi();

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "START");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        String sessionId = intent.getStringExtra("session_id");
        String token = intent.getStringExtra("token");
        Log.e(TAG, "Token: " + token);
        Log.e(TAG, "Session: " + sessionId);

        yandexStationApi.init(sessionId, token);

        setContentView(R.layout.activity_main);

//        try {
//            yandexStationApi.send(new URL("https://youtu.be/VB5xtFwS-fg"));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendURL(intent);
            finish();
        }
    }

    void handleSendURL(Intent intent) {
        try {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.e(TAG, "YOUTUBE > " + sharedText);

            if (sharedText != null) {
                URL url = new URL(sharedText);
                yandexStationApi.send(url);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void npreviewActivity() {
        Intent intent = new Intent(MainActivity.this, WebActivity.class);
        startActivity(intent);
        finish();
    }
}
