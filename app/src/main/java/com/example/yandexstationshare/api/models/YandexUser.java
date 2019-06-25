package com.example.yandexstationshare.api.models;

public class YandexUser {
    private String token;
    private String session;
    private Station station;

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

    public void setStation(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
