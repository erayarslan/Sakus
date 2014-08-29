package com.guguluk.sakus.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by guguluk on 29.08.2014.
 */
public class SapiRestAdapter {
    private Gson gson;

    private RestAdapter restAdapter;

    public SapiRestAdapter() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint("http://sapi.8cook.in")
                .setConverter(new GsonConverter(gson))
                .build();
    }

    public RestAdapter getRestAdapter() {
        return this.restAdapter;
    }
}
