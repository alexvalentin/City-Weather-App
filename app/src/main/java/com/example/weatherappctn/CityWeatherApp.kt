package com.example.weatherappctn

import android.app.Application

class CityWeatherApp: Application() {
    companion object {
        lateinit var instance : CityWeatherApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}