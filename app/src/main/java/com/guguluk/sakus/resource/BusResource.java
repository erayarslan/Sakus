package com.guguluk.sakus.resource;

import com.guguluk.sakus.service.BusService;
import com.guguluk.sakus.util.SapiRestAdapter;

import retrofit.Callback;

/**
 * Created by guguluk on 29.08.2014.
 */
public class BusResource {
    private BusService busService;

    public BusResource() {
        busService = new SapiRestAdapter().getRestAdapter().create(BusService.class);
    }

    public void getBus(String name, String id, Callback callback) {
        busService.getBus(name, id, callback);
    }
}
