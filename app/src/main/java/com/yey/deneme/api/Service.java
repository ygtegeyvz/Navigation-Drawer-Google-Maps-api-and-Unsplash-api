package com.yey.deneme.api;

/**
 * Created by ege on 27.11.2017.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.yey.deneme.Model.Model;
import com.yey.deneme.Model.PhotoModel;


public interface Service {
    String BASE_URL = "https://api.unsplash.com/";

    @GET("photos")
    Call<List<Model>> Modelleri_Al(@Query("client_id") String client_id);

    @GET("photos/{id}/")
    Call<PhotoModel> Fotolari_Al(@Path("id") String photo_id, @Query("client_id") String client_id);

    @GET("photos/random/")
    Call<List<PhotoModel>> Random_Al(@Query("client_id") String client_id, @Query("count") String count);
}
