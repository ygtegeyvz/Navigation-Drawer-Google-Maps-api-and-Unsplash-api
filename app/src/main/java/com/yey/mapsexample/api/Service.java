package com.yey.mapsexample.api;

import com.yey.mapsexample.Model.Model;
import com.yey.mapsexample.Model.PhotoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ege on 23.11.2017.
 */

public interface Service {
    String BASE_URL = "https://api.unsplash.com/";
    @GET("photos")
    Call<List<Model>> Modelleri_Al(@Query("client_id") String client_id);

    @GET("photos/{id}/")
    Call<PhotoModel> Fotolari_Al(@Path("id") String photo_id, @Query("client_id") String client_id);

    @GET("photos/random/")
    Call<List<PhotoModel>>Random_Al(@Query("client_id") String client_id,@Query("count")  String count);
}
