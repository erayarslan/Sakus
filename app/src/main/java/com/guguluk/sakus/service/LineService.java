package com.guguluk.sakus.service;

import com.guguluk.sakus.dto.Line;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by guguluk on 29.08.2014.
 */
public interface LineService {
    @GET("/line/{name}")
    void getLine(@Path("name") String name, Callback<Line> callback);
}
