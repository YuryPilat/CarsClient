package com.example.carclient.managers;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class CustomAuthenticator implements Authenticator {

    private final HttpManager _httpManager;

    public CustomAuthenticator(HttpManager httpManager) {
        _httpManager = httpManager;
    }

    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {
        if (response.code() != HttpURLConnection.HTTP_UNAUTHORIZED) return null;

        String newAccessToken = _httpManager.refreshToken();
        return response.request().newBuilder()
                .header("Authorization", newAccessToken)
                .build();
    }
}
