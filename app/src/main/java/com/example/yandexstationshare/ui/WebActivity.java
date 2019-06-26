package com.example.yandexstationshare.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.yandexstationshare.api.models.Station;
import com.example.yandexstationshare.api.models.YandexUser;
import com.example.yandexstationshare.api.request.RequestSend;
import com.example.yandexstationshare.api.request.RequestStations;
import com.example.yandexstationshare.logger.DefautLogger;
import com.example.yandexstationshare.logger.Logger;
import com.example.yandexstationshare.storage.Storage;
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
    final private Storage<Station> stationStorage = new Storage<>(this);

    final class SendTask extends AsyncTask<URL, Void, URL> {
        @Override
        protected URL doInBackground(URL[] objects) {
            try {
                api.send(objects[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return objects[0];
        }

        @Override
        protected void onPostExecute(URL url) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    url.toString(),
                    Toast.LENGTH_SHORT
            );
            toast.show();

            super.onPostExecute(url);
        }
    }

    final class SendStations extends AsyncTask<YandexUser, Void, Station> {
        @Override
        protected Station doInBackground(YandexUser[] objects) {
            RequestStations requestStations = new RequestStations(objects[0]);
            try {
                requestStations.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return requestStations.getStations().get(0);
        }

        @Override
        protected void onPostExecute(Station station) {
            //register station
            stationStorage.set("station", station);
            api.getUser().setStation(station);

            Toast toast = Toast.makeText(getApplicationContext(),
                    station.getName(),
                    Toast.LENGTH_SHORT
            );
            toast.show();

            onAuth();

            super.onPostExecute(station);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!api.isAuth()) {
            final WebView myWebView = new WebView(this);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new MyWebViewClient((YandexUser user) -> {
                api.authorization(user);
                if (stationStorage.get("station") == null) {
                    //request get station
                    (new SendStations()).execute(user);
                    myWebView.setVisibility(View.GONE);

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Авторизован",
                            Toast.LENGTH_SHORT
                    );


                    toast.show();
                    myWebView.setVisibility(View.GONE);
                    onAuth();
                }
            }));


            myWebView.loadUrl("https://passport.yandex.ru/auth/welcome");
            setContentView(myWebView);
        } else {
            onAuth();
        }
    }

    protected void onAuth() {
        setContentView(R.layout.activity_web);
        YandexUser user = api.getUser();
        setContentView(R.layout.activity_web);
        TextView text = (TextView) findViewById(R.id.textView);
        TextView text1 = (TextView) findViewById(R.id.textView2);

        text.setText("Token: " + user.getToken());
        text1.setText("Session: " + user.getSession());
        Station station = (Station) stationStorage.get("station");

        text1.setText("Station: " + station.getName());

        if (send()) {
            finish();
        }
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

                SendTask task = new SendTask();
                task.execute(url);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
