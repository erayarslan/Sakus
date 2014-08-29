package com.guguluk.sakus.service;

import com.guguluk.sakus.dto.City;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by guguluk on 29.08.2014.
 */
public interface CityService {
    @GET("/")
    void getCity(Callback<City> callback);
}
