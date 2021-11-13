package com.example.weatherappctn.usecase

import com.google.gson.annotations.SerializedName


data class Main (
    @SerializedName("temp")
    var temp : Double,

    @SerializedName("temp_min")
    var tempMin : Double,

    @SerializedName("temp_max")
    var tempMax : Double,

    @SerializedName("feels_like")
    var feelsLike : Double,

    @SerializedName("humidity")
    var humidity : Int,

    @SerializedName("pressure")
    var pressure : Int

)