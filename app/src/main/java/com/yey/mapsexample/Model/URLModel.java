package com.yey.mapsexample.Model;

import com.google.gson.annotations.SerializedName;

        import com.google.gson.annotations.SerializedName;

/**
 * Created by ege on 22.11.2017.
 */

public class URLModel {
    @SerializedName("small")
    public String small;
    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }
}
