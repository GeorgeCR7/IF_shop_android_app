package com.example.if_shop_android_app.app.networking;

import com.example.if_shop_android_app.app.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("lat") double latitude,
                                     @Query("lon") double longitude,
                                     @Query("appid") String apiKey);
}
