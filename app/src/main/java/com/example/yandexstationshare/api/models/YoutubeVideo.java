package com.example.yandexstationshare.api.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeVideo {
    @SerializedName("player_id")
    final private String PlayerId = "youtube";
    @SerializedName("provider_item_id")
    private String ProviderItemId = "http://www.youtube.com/watch?v=";
    @SerializedName("provider_name")
    final private String ProviderName = "youtube.com";
    @SerializedName("source_host")
    final private String SourceHost = "www.youtube.com";
    @SerializedName("type")
    final private String Type = "video";
    @SerializedName("visible_url")
    private String VisibleUrl = "http://www.youtube.com/watch?v=";

    public YoutubeVideo(String id) {
        ProviderItemId += String.valueOf(id);
        VisibleUrl += String.valueOf(id);
    }
}
