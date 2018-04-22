package com.example.carclient.models;

import com.google.gson.annotations.SerializedName;

public class TokenModel {

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("token_type")
    public String token_type;

    @SerializedName("expires_in")
    public int expires_in;
}