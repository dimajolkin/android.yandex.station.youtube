package com.example.yandexstationshare.storage;

import android.content.Context;

import com.example.yandexstationshare.api.models.Station;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

public class StorageTest extends TestCase {

    public void testCreate() {
        Context context = mock(Context.class);
        Storage<Station> stationStorage = new Storage<Station>(context) {
            @Override
            protected String readFile(String key) {
                return "{\"id\":\"1234567\",\"name\":\"Яндекс Станция\"}";
            }
        };

        Station station = stationStorage.get("station");
        assertEquals(station.getId(), "1234567");
        assertEquals(station.getName(), "Яндекс Станция");
    }
}
