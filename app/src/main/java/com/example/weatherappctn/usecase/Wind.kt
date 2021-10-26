package com.example.weatherappctn.usecase

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed")
    var speed : Double
)


