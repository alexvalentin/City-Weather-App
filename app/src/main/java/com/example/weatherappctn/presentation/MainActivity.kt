package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherappctn.usecase.ApiInterface
import com.example.weatherappctn.usecase.Example
import com.example.weatherappctn.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    //var url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}"
    private var apikey = "a798f7097c76a6b41da1ef25b4f77e67"

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            getWeatherData(
                binding.cityEditText.text.toString().trim())
        }
    }

    private fun getWeatherData(string : String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi : ApiInterface = retrofit.create(ApiInterface::class.java)

        val exampleCall : Call<Example?>? = myApi.getWeatherData(binding.cityEditText.text.toString().trim(), apikey)

        exampleCall?.enqueue(object : Callback<Example?> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call : Call<Example?>, response : Response<Example?>) {

                if (response.code() == 404) {
                    Toast.makeText(this@MainActivity, "Please Enter a valid City", Toast.LENGTH_LONG).show()
                } else if (!response.isSuccessful) {
                    Toast.makeText(this@MainActivity, response.code().toString() + " ", Toast.LENGTH_LONG).show()
                    return
                }


                // Get data from Main class
                val myData = response.body()

                // Get data from temp
                val temp = myData?.main?.temp
                val temperature = (temp?.minus(273.15))!!.toInt()
                binding.degreesTv.text = "$temperature C"

                //Get data from humidity
                val humid = myData.main.humidity
                binding.humidityTv.text = humid.toString()

                // get data from feels like
                val feelsLike = myData.main.feelsLike
                val temperatureFL = (feelsLike.minus(273.15)).toInt()
                binding.descriptionTv.text = "Feels Like: $temperatureFL C"

                // wind speed
                val windSpeed = myData.wind.speed
                binding.windTv.text = "wind speed: $windSpeed"

                // status weather

                val statusWeather = myData.weather[0].main
                binding.statusTv.text = "Status weather: $statusWeather"

            }

            override fun onFailure(call : Call<Example?>, t : Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


}