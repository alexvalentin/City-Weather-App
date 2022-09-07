package com.example.weatherappctn.usecase

import com.google.gson.annotations.SerializedName

data class Example (
    @SerializedName("main")
    var main : Main,

    @SerializedName("wind")
    var wind : Wind,

    @SerializedName("weather")
    var weather : List<Weather>,

    @SerializedName("dt")
    var dt : Long,

    @SerializedName("timezone")
    var timezone : Long,

    @SerializedName("name")
    var nameCity: String,

    @SerializedName("sys")
    var sys: Sys
)