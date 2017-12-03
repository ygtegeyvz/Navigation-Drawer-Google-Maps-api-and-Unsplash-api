package com.yey.deneme.Model;

/**
 * Created by ege on 27.11.2017.
 */

import com.google.gson.annotations.SerializedName;

        import com.google.android.gms.maps.model.LatLng;
        import com.google.gson.annotations.SerializedName;

        import com.google.gson.annotations.SerializedName;

/**
 * Created by ege on 22.11.2017.
 */

public class Position {

    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
