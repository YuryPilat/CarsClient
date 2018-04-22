package com.example.carclient.interfaces;

import com.example.carclient.models.BaseModel;
import com.example.carclient.models.TokenModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IServerApi {

    @GET("cities")
    Call<List<BaseModel>> getCities();

    @GET("classes")
    Call<List<BaseModel>> getClasses();

    @GET("ShowRooms")
    Call<List<BaseModel>> getDealers();

    @Headers({
            "Authorization: Basic Q3VzdG9tR3JhbnRUeXBlQ2xpZW50SWQ6Q3VzdG9tR3JhbnRUeXBlQ2xpZW50U2VjcmV0",
            "Content-Type: application/x-www-form-urlencoded"
            })

    @FormUrlEncoded
    @POST("http://identity-server.test.intravision.ru/core/connect/token/")
    Call<TokenModel> getToken(
                    @Field("scope") String scope,
                    @Field("grant_type") String grantType);


}
