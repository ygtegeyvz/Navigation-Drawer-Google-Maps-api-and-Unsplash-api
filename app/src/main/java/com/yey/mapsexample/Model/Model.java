package com.yey.mapsexample.Model;

/**
 * Created by ege on 23.11.2017.
 */

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

public class Model {
    public String id;
    public String created_at;
    public int width;
    public int height;
    public Model(String id,String created_at,int width,int height){
        this.id = id;
        this.created_at = created_at;
        this.width = width;
        this.height = height;

    }
    @SerializedName("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @SerializedName("created_at")
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    @SerializedName("width")
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    @SerializedName("height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
