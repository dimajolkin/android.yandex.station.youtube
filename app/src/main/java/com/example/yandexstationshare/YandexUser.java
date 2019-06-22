package com.example.yandexstationshare;

public class YandexUser {
    private String token;
    private String session;
    private YandexStation station;

    public YandexUser(String token, String session) {
        this.token = token;
        this.session = session;
    }

    public String getToken() {
        return token;
    }

    public String getSession() {
        return session;
    }

    public void setStation(YandexStation station) {
        this.station = station;
    }

    public YandexStation getStation() {
        return station;
    }
}
