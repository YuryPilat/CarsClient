package com.example.carclient.models;

import com.google.gson.annotations.SerializedName;

public class BaseModel {

    @SerializedName("Id")
    public int id;

    @SerializedName("Name")
    public String name;
}