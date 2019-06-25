package com.example.yandexstationshare.api;

import androidx.annotation.NonNull;

import com.example.yandexstationshare.api.models.VideoMessage;
import com.example.yandexstationshare.api.models.YandexUser;
import com.example.yandexstationshare.api.models.YoutubeVideo;
import com.example.yandexstationshare.api.request.RequestSend;
import com.example.yandexstationshare.logger.Logger;

import java.net.URL;

public class Api {

    private YandexUser user;
    private Logger logger;

    public Api(@NonNull Logger logger) {
        this.logger = logger;
    }

    public void authorization(@NonNull YandexUser user) {
        this.user = user;
    }

    public void send(URL url) {
        String videoId = YoutubeVideo.parseVideoId(url);
        RequestSend requestSend = new RequestSend(new VideoMessage(user, new YoutubeVideo(videoId)));

        new Thread(() -> {
            try {
                requestSend.execute();
            } catch (Exception ex) {
                logger.exception(ex);
            }
        }).start();
    }

    public String getVideoURL() {
        return "https://yandex.ru/video/touch/search?text=youtube";
    }
}
