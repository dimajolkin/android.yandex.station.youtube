package com.example.yandexstationshare;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.mockito.Mockito.mock;

public class YandexStationApiTest extends TestCase {

    @Test
    public void testSend() throws MalformedURLException, IOException {
        YandexStationApi api = mock(YandexStationApi.class);
        YandexUser user = new YandexUser("token", "session");

        AsyncGetStationsRequest request =  mock(AsyncGetStationsRequest.class);
        request.execute(new Object[]{api, user});
    }
}
