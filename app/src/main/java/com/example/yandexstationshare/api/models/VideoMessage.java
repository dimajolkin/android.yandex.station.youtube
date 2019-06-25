package com.example.yandexstationshare.api.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoMessage {
    private YoutubeVideo video;
    private YandexUser user;

    public VideoMessage(YandexUser user, YoutubeVideo video) {
        this.user = user;
        this.video = video;
    }

    public YandexUser getUser() {
        return user;
    }

    public JsonObject toJson() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("device", user.getStation().getId());
        jsonObject.add("msg", (new Gson().toJsonTree(video).getAsJsonObject()));

        return jsonObject;
    }
}
