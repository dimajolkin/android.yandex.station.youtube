package com.example.yandexstationshare.api;

import androidx.annotation.NonNull;

import com.example.yandexstationshare.api.models.YandexUser;

import java.net.URL;

public class Api {

    private YandexUser user;

    public void authorization(@NonNull  YandexUser user) {
        this.user = user;
    }

    public void send(URL url) {
        //
    }

    public String getVideoURL() {
        return "https://yandex.ru/video/touch/search?text=youtube";
    }
}
