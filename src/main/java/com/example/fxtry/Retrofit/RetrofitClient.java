package com.example.fxtry.Retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String baseUrl = "http://localhost:9004/";

    public static Retrofit getInstanceRetrofit(){

        if (retrofit == null){
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(600, TimeUnit.SECONDS);


            retrofit= new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}
