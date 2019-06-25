package com.example.yandexstationshare.api.request;

import com.example.yandexstationshare.api.models.VideoMessage;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestSend implements RequestInterface {

    final private static String HOST = "https://yandex.ru";
    private VideoMessage message;
    private static class Result {
        @SerializedName("code")
        private int code;
        @SerializedName("msg")
        private String message;
        @SerializedName("status")
        private String status;


        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
        }
    }

    public RequestSend(VideoMessage message) {
        this.message = message;
    }

    protected String getResponse() throws Exception {
        URL url = new URL(HOST + "/video/touch/search?text=youtube");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        conn.setRequestProperty("cookie", "Session_id=" + message.getUser().getSession());
        conn.setRequestProperty("x-csrf-token", message.getUser().getToken());

        setPostRequestContent(conn, message.toJson());
        conn.connect();

        if (conn.getResponseCode() != 200) {
            throw new Exception(conn.getResponseMessage());
        }

        return IOUtils.toString(conn.getInputStream(), "UTF-8");
    }

    @Override
    public void execute() throws Exception {
        String response = getResponse();
        Result result = (new Gson()).fromJson(response, Result.class);
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream op = conn.getOutputStream();
        op.write(jsonObject.toString().getBytes());
        op.close();
    }
}
