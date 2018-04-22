package com.example.carclient.managers;

import android.support.annotation.NonNull;

import com.example.carclient.Root;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomInterceptor implements Interceptor {

    private final String AUTHORIZATION = "authorization";
    private final String ACCESS_TOKEN = "AccessToken";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (authorizationTokenIsEmpty() || alreadyHasAuthorizationHeader(originalRequest)) {
            return chain.proceed(originalRequest);
        }

        Request authorisedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION, getAuthorizationValue())
                .build();
        return chain.proceed(authorisedRequest);
    }

    private boolean authorizationTokenIsEmpty() {
        return Root.getString(ACCESS_TOKEN, null) == null;
    }

    private boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        return originalRequest.header(AUTHORIZATION) != null;
    }

    private String getAuthorizationValue() {
        return Root.getString(ACCESS_TOKEN, null);
    }
}