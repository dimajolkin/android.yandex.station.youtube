package com.example.yandexstationshare.api.models;

import com.google.gson.Gson;

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

    public JSONObject toJson() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("device", user.getStation().getId());
        jsonObject.put("msg", (new Gson()).toJson(video));

        return jsonObject;
    }
}
