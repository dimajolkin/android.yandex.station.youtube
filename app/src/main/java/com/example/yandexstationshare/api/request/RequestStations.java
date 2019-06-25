package com.example.yandexstationshare.api.request;

import com.example.yandexstationshare.api.models.YandexUser;
import com.example.yandexstationshare.api.models.Station;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RequestStations implements RequestInterface {

    private volatile YandexUser user;
    private List<Station> stations = new ArrayList<>();

    public RequestStations(YandexUser user) {
        this.user = user;
    }

    protected String getResponse() throws Exception {

        URL url = new URL("https://quasar.yandex.ru/devices_online_stats");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        conn.setRequestProperty("cookie", "Session_id=" + user.getSession());
        conn.connect();

        if (conn.getResponseCode() != 200) {
            throw new Exception(conn.getResponseMessage());
        }
        return IOUtils.toString(conn.getInputStream(), "UTF-8");
    }

    @Override
    public void execute() throws Exception {
        String json = getResponse();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        stations.clear();
        for (JsonElement item : jsonObject.getAsJsonArray("items")) {
            stations.add(gson.fromJson(item, Station.class));
        }

    }

    public List<Station> getStations() {
        return stations;
    }
}
