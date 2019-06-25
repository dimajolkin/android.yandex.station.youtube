package com.example.yandexstationshare.api.models;

import com.google.gson.Gson;

import junit.framework.TestCase;

public class YoutubeVideoTest extends TestCase {
    final private static String VIDEO_JSON = "{" +
            "\"player_id\":\"youtube\"," +
            "\"provider_item_id\":\"http://www.youtube.com/watch?v\\u003d12345\"," +
            "\"provider_name\":\"youtube.com\"," +
            "\"source_host\":\"www.youtube.com\"," +
            "\"type\":\"video\"," +
            "\"visible_url\":\"http://www.youtube.com/watch?v\\u003d12345\"" +
            "}";

    public void testCreate() {
        YoutubeVideo video = new YoutubeVideo("12345");
        Gson gson = new Gson();
        String json = gson.toJson(video);

        assertEquals(json, VIDEO_JSON);
    }
}
