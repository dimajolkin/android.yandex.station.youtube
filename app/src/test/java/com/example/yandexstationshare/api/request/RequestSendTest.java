package com.example.yandexstationshare.api.request;

import com.example.yandexstationshare.api.models.YandexUser;
import com.example.yandexstationshare.api.models.VideoMessage;
import com.example.yandexstationshare.api.models.YoutubeVideo;

import junit.framework.TestCase;

import org.junit.Test;

public class RequestSendTest extends TestCase {
    final private static String SUCCESS_RESPONSE = "{\"status\":\"play\",\"msg\":\"success\",\"code\":1}";

    @Test
    public void testExecute() throws Exception {
        YoutubeVideo video = new YoutubeVideo("12345");
        YandexUser user = new YandexUser("", "");
        VideoMessage message = new VideoMessage(user, video);

        RequestSend requestSend = new RequestSend(message) {
            @Override
            protected String getResponse() throws Exception {
                return SUCCESS_RESPONSE;
            }
        };
        requestSend.execute();
    }
}
