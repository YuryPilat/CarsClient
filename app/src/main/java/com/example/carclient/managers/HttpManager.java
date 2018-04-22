package com.example.carclient.managers;

import com.example.carclient.Root;
import com.example.carclient.interfaces.IServerApi;
import com.example.carclient.models.TokenModel;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private final String URL = "http://3plus-authless.test.intravision.ru/api/";
    private final String PROFILE = "profile";
    private final String GRANT_TYPE = "custom_client_credentials";
    private final String ACCESS_TOKEN = "AccessToken";
    public IServerApi serverApi;

    public HttpManager(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new CustomInterceptor())
                .authenticator(new CustomAuthenticator(this))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverApi = retrofit.create(IServerApi.class);
    }

    String refreshToken() throws IOException {
        Response<TokenModel> response = serverApi.getToken(PROFILE, GRANT_TYPE).execute();

        TokenModel tokenModel = response.body();
        if (tokenModel == null) {
            return null;
        }

        String token = tokenModel.token_type + " " + tokenModel.access_token;
        Root.setString(ACCESS_TOKEN, token);
        return token;

    }
}