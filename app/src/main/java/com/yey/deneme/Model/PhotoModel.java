package com.yey.deneme.Model;

/**
 * Created by ege on 27.11.2017.
 */

import com.google.gson.annotations.SerializedName;


public class PhotoModel {

    public String id;
    public String created_at;
    public int width;
    public int height;
    public LocationModel location;
    public URLModel urls;

    @SerializedName("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  String description;
    @SerializedName("urls")
    public URLModel getUrlModel() {
        return urls;
    }

    public void setUrlModel(URLModel urlModel) {
        this.urls = urlModel;
    }

    @SerializedName("location")
    public LocationModel getLocationModel() {
        return location;
    }

    public void setLocationModel(LocationModel location) {
        this.location = location;
    }

    public PhotoModel(String id,String created_at,int width,int height){
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

