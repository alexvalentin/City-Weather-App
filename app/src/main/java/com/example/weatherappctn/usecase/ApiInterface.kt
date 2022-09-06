package com.example.weatherappctn.usecase

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    fun getWeatherData(
        @Query("q") cityName : String?,
        @Query("units") units : String,
        @Query("appid") apikey : String?
    ) : Call<Example?>?
}