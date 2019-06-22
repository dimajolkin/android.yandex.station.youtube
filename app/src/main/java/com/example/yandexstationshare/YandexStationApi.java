package com.example.yandexstationshare;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;


public class YandexStationApi {
    //@TODO choice your yandex station ID
    final private static String YANDEX_STATION_DEVICE_ID = "04107884c9144c12030f";
    private String host = "https://yandex.ru";
    private YandexUser user;

    public void init(YandexUser user) {
        user.setStation(new YandexStation(YANDEX_STATION_DEVICE_ID));
        //(new AsyncGetStationsRequest()).execute(this, user);

        this.user = user;
    }

    public void send(URL youtubeURL) {
        String videoId = youtubeURL.getPath().substring(1);
        (new AsyncRequest()).execute(this, new Message(
                this.user,
                new YoutubeVideo(videoId)
        ));
    }

    public String getVideoStation()
    {
        return host + "/video/station";
    }

    public String getStationsItems()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return "https://quasar.yandex.ru/devices_online_stats?_=" + String.valueOf(timestamp.getTime());
    }

    public String getVideoURL() {
        return host + "/video/touch/search?text=youtube";
    }
}

class Message {
    private VideoInterface video;
    private YandexUser user;

    public Message(YandexUser user, VideoInterface video) {
        this.user = user;
        this.video = video;
    }

    public YandexUser getUser() {
        return user;
    }

    public VideoInterface getVideo() {
        return video;
    }
}

interface VideoInterface {
    public JSONObject getJson() throws JSONException;
}

class YoutubeVideo implements VideoInterface {
    final private static String PlayerId = "youtube";
    final private static String ProviderItemId = "http://www.youtube.com/watch?v=";
    final private static String ProviderName = "youtube.com";
    final private static String SourceHost = "www.youtube.com";
    final private static String Type = "video";
    final private static String VisibleUrl = "http://www.youtube.com/watch?v=";
    private String id;

    public YoutubeVideo(String id) {
        this.id = id;
    }

    public JSONObject getJson() throws JSONException {
        JSONObject msg = new JSONObject();
        msg.put("player_id", PlayerId);
        msg.put("provider_item_id", ProviderItemId + id);
        msg.put("provider_name", ProviderName);
        msg.put("source_host", SourceHost);
        msg.put("type", Type);
        msg.put("visible_url", VisibleUrl + id);

        return msg;
    }
}


class AsyncGetStationsRequest extends AsyncTask<Object, Object, Object> {

    @Override
    protected String doInBackground(Object[] objects) {
        YandexStationApi api = (YandexStationApi) objects[0];
        YandexUser user = (YandexUser) objects[1];

        try {
            Log.e("Main", "SUPER FILED");
            URL url = new URL(api.getStationsItems());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            //conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.setRequestProperty("cookie", "Session_id=" + user.getSession());
            //conn.setRequestProperty("x-csrf-token", user.getToken());
            conn.connect();

            Log.e("Main", conn.getResponseMessage());
            Log.e("Main", "????");
            return conn.getResponseMessage();

        } catch (IOException e) {
            Log.e("Main", e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream op = conn.getOutputStream();
        op.write(jsonObject.toString().getBytes());
        op.close();
    }
}


class AsyncRequest extends AsyncTask<Object, Object, Object> {

    @Override
    protected String doInBackground(Object[] objects) {
        YandexStationApi api = (YandexStationApi) objects[0];
        Message message = (Message) objects[1];

        try {
            URL url = new URL(api.getVideoStation());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            conn.setRequestProperty("cookie", "Session_id=" + message.getUser().getSession());
            conn.setRequestProperty("x-csrf-token", message.getUser().getToken());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device", message.getUser().getStation().getId());
            jsonObject.put("msg", message.getVideo().getJson());

            setPostRequestContent(conn, jsonObject);
            conn.connect();
            return conn.getResponseMessage();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream op = conn.getOutputStream();
        op.write(jsonObject.toString().getBytes());
        op.close();
    }
}

