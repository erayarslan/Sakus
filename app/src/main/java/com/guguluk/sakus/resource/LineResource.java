package com.guguluk.sakus.resource;

import com.guguluk.sakus.service.LineService;
import com.guguluk.sakus.util.SapiRestAdapter;

import retrofit.Callback;

/**
 * Created by guguluk on 29.08.2014.
 */
public class LineResource {
    private LineService lineService;

    public LineResource() {
        lineService = new SapiRestAdapter().getRestAdapter().create(LineService.class);
    }

    public void getLine(String name, Callback callback) {
        lineService.getLine(name, callback);
    }
}
