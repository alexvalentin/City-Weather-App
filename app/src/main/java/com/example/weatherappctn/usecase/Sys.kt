package com.example.weatherappctn.usecase

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("sunrise")
    var sunrise : Long,

    @SerializedName("sunset")
    var sunset : Long
)