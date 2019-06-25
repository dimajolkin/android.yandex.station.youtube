package com.example.yandexstationshare.ui.webview;

import com.example.yandexstationshare.api.models.YandexUser;

@FunctionalInterface
public interface EventAfterRegister {
    public void register(YandexUser user);
}
