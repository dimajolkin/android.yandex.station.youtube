package com.example.yandexstationshare.api.request;

import com.example.yandexstationshare.api.models.YandexUser;
import com.example.yandexstationshare.api.models.Station;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

import java.util.List;

public class RequestStationTest extends TestCase {

    final private static String MOCK_RESPONSE = "{\"items\":[{\"id\":\"12\",\"name\":\"Яндекс Станция\",\"online\":true,\"platform\":\"yandexstation\",\"screen_capable\":true,\"screen_present\":true}],\"status\":\"ok\"}";

    public void testConvert() {
        String json = MOCK_RESPONSE;
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        assertEquals(jsonObject.getAsJsonArray("items").size(), 1);
        for (JsonElement item: jsonObject.getAsJsonArray("items")) {
            Station station = gson.fromJson(item, Station.class);
            assertEquals(station.getId(), "12");
            assertEquals(station.getName(), "Яндекс Станция");
        }
    }

    public void testSendRequest() throws Exception  {
        YandexUser user = new YandexUser("", "");
        RequestStations requestStations = new RequestStations(user) {
            @Override
            protected String getResponse() {
                return MOCK_RESPONSE;
            }
        };

        requestStations.execute();
        List<Station> stations = requestStations.getStations();
        assertEquals(stations.size(), 1);

        for (Station station: stations) {
            assertEquals(station.getId(), "12");
            assertEquals(station.getName(), "Яндекс Станция");
        }
    }
//    public void testExecute() throws Exception {
//        String sessionId = "3:1561385236.5.0.1560632514893:kiQOww:56.1|33079297.0.2|1130000021322355.130501.2.2:130501|201168.969758.sMWgEkDsJCkg9p-GbL0J-FRZDw0";
//        YandexUser user = new YandexUser("123", sessionId);
//        RequestStations requestStations = new RequestStations(user);
//        requestStations.execute();
//
//        List<Station> stations = requestStations.getStations();
//        assertEquals(stations.size(), 1);
//    }
}
