package com.example.weatherappctn.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingPage(
    @StringRes
    val title : Int,
    @StringRes
    val description : Int,
    @DrawableRes
    val image : Int
)
