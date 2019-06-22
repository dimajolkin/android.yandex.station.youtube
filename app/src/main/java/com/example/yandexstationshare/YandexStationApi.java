package com.example.yandexstationshare;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class YandexStationApi {

    private String sessionId;
    private String token;
    private boolean isInit = false;


    public void init(String sessionId, String token) {
        if (!isInit) {
            this.sessionId = sessionId;
            this.token = token;

            this.isInit = true;
        }
    }

    protected static class AsyncRequest extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... items) {
            String videoId = items[2];

            try {

                URL url = new URL("https://yandex.ru/video/station");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
                conn.setRequestProperty("cookie", "Session_id=" + items[0]);
                conn.setRequestProperty("x-csrf-token", items[1]);

                JSONObject jsonObject = buildJsonObject(videoId);
                setPostRequestContent(conn, jsonObject);


                // 4. make POST request to the given URL
                conn.connect();

                Log.e("Main", conn.toString());
                Log.e("Main", String.valueOf(conn.getResponseCode()));

                return conn.getResponseMessage();
            } catch (IOException | JSONException e) {
                //e.printStackTrace();
            }

            return "";
        }

        protected JSONObject buildJsonObject(String viderId) throws JSONException {
            JSONObject msg = new JSONObject();
            msg.put("player_id", "youtube");
            msg.put("provider_item_id", "http://www.youtube.com/watch?v=" + viderId);
            msg.put("provider_name", "youtube.com");
            msg.put("source_host", "www.youtube.com");
            msg.put("type", "video");
            msg.put("visible_url", "http://www.youtube.com/watch?v=" + viderId);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device", "04107884c9144c12030f");
            jsonObject.put("msg", msg);

            return jsonObject;
        }

        private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

            OutputStream op = conn.getOutputStream();
            op.write(jsonObject.toString().getBytes());
            op.close();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Main", s);
        }
    }


    public void send(URL youtubeURL) {
        String videoId = youtubeURL.getPath().substring(1);

        (new AsyncRequest()).execute(sessionId, token, videoId);
    }


}
