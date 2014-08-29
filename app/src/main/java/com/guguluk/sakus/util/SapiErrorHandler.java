package com.guguluk.sakus.util;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by guguluk on 29.08.2014.
 */
public class SapiErrorHandler implements ErrorHandler {
    @Override public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        if (r != null && r.getStatus() == 401) {
            return cause;
        } return cause;
    }
}
