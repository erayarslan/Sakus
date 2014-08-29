package com.guguluk.sakus.resource;

import com.guguluk.sakus.service.CityService;
import com.guguluk.sakus.util.SapiRestAdapter;

import retrofit.Callback;

/**
 * Created by guguluk on 29.08.2014.
 */
public class CityResource {
    private CityService cityService;

    public CityResource() {
        cityService = new SapiRestAdapter().getRestAdapter().create(CityService.class);
    }

    public void getCity(Callback callback) {
        cityService.getCity(callback);
    }
}
