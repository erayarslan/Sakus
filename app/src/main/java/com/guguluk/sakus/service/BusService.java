package com.guguluk.sakus.service;

import com.guguluk.sakus.dto.Bus;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by guguluk on 29.08.2014.
 */
public interface BusService {
    @GET("/line/{name}/bus/{id}")
    void getBus(@Path("name") String name,@Path("id") String id,Callback<Bus> callback);
}
