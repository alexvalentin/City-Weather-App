package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.weatherappctn.databinding.FragmentWeatherDisplayBinding
import com.example.weatherappctn.usecase.ApiInterface
import com.example.weatherappctn.usecase.Example
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class WeatherDisplayFragment : Fragment() {

    private lateinit var binding : FragmentWeatherDisplayBinding

    //private val onBoardingPages = mutableListOf<OnBoardingPage>()
    //var url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}"
    private var apikey = "a798f7097c76a6b41da1ef25b4f77e67"
    private val units = "metric"


    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentWeatherDisplayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insetterStationBar()

        binding.button.setOnClickListener {
            getWeatherData(
                binding.cityEditText.text.toString().trim()
            )
        }
    }

    private fun insetterStationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(statusBars = true))
            .applyToView(binding.sunsetTv)

    }

    private fun getWeatherData(string : String?) {
        if (string != null) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val myApi : ApiInterface = retrofit.create(ApiInterface::class.java)

            val exampleCall : Call<Example?>? = myApi.getWeatherData(binding.cityEditText.text.toString().trim(), units, apikey)

            exampleCall?.enqueue(object : Callback<Example?> {

                @SuppressLint("SetTextI18n", "SimpleDateFormat", "DefaultLocale")
                override fun onResponse(call : Call<Example?>, response : Response<Example?>) {

                    if (response.code() == 404) {
                        Toast.makeText(context, "Please Enter a valid City", Toast.LENGTH_LONG).show()
                    } else if (!response.isSuccessful) {
                        Toast.makeText(context, response.code().toString() + " ", Toast.LENGTH_LONG).show()
                        return
                    }

                    // Get data from Main class
                    val myData = response.body()

                    // City name
                    val nameCity = myData?.nameCity
                    binding.cityDisplayTv.text = "$nameCity"

                    // Get data from temperature
                    val temperature = myData?.main?.temp
                    binding.degreesTv.text = "${temperature?.toInt()}\u2103"

                    // Get minimum temperature of the day
                    val tempMin = myData?.main?.tempMin
                    binding.tempMinTv.text = "Minimum temperature\n${tempMin?.toInt()}\u2103"

                    // Get maximum temperature of the day
                    val tempMax = myData?.main?.tempMax
                    binding.tempMaxTv.text = "Maximum temperature\n${tempMax?.toInt()}\u2103"

                    // Description Weather
                    val descriptionStatusWeather = myData!!.weather[0].description
                    binding.descriptionStatusWeatherTv.text = "Description weather\n${descriptionStatusWeather.capitalize()}"

                    // Get data from feels like
                    val feelsLike = myData.main.feelsLike
                    binding.feelsLikeTempTv.text = "Feels Like\n${feelsLike.toInt()}℃"

                    //Get data from humidity
                    val humidity = myData.main.humidity
                    binding.humidityTv.text = "Humidity level\n$humidity%"

                    // Wind speed
                    val windSpeed = myData.wind.speed
                    binding.windTv.text = "Wind speed\n${windSpeed} m/sec"

                    // status weather
                    val statusWeather = myData.weather[0].main
                    binding.statusWeatherTv.text = statusWeather


                    val timezone = myData.timezone
                    Toast.makeText(context, "timezone is: $timezone", Toast.LENGTH_LONG).show()


                    // Sunrise hour
                    val sunrise = myData.sys.sunrise
                    val sunrise1 = Date((sunrise -7200 + timezone) * 1000)
                    val simpleDateFormat = SimpleDateFormat("HH:mm")


                    val sunriseTime = simpleDateFormat.format(sunrise1)
                    binding.sunriseTv.text = "Sunrise\n$sunriseTime"

                    // Sunset hour
                    val sunset = myData.sys.sunset
                    val sunset1 = Date((sunset-7200 + timezone) * 1000)

                    val sunsetTime = simpleDateFormat.format(sunset1)
                    binding.sunsetTv.text = "Sunset\n$sunsetTime"

                    // Set icon for status weather
                    val icon = myData.weather[0].icon
                    val iconUrl = "http://openweathermap.org/img/w/$icon.png"
                    Glide.with(context!!).load(iconUrl).into(binding.iconStatusWeather)

                    // Current day
                    val timeZone = myData.dt
                    val date = Date((timeZone-7200 + timezone) * 1000)
                    val ceva = simpleDateFormat.format(date)
                    binding.localHourTv.text = ceva


                }

                override fun onFailure(call : Call<Example?>, t : Throwable) {

                }
            })
        }
    }
}