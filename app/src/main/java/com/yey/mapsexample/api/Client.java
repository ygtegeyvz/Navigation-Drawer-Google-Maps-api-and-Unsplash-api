package com.yey.mapsexample.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ege on 23.11.2017.
 */

public class Client {
    public static final String BASE_URL = "https://api.unsplash.com/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit==null){

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(600, TimeUnit.SECONDS)
                    .connectTimeout(600,TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
