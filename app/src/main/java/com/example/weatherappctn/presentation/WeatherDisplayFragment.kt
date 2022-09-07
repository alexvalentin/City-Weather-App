package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherappctn.databinding.FragmentWeatherDisplayBinding
import com.example.weatherappctn.usecase.ApiInterface
import com.example.weatherappctn.usecase.Example
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherDisplayFragment : Fragment() {

    private lateinit var binding : FragmentWeatherDisplayBinding
    //private val onBoardingPages = mutableListOf<OnBoardingPage>()
    //var url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}"
    private var apikey = "secret_api"

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentWeatherDisplayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    Toast.makeText(context, "Please Enter a valid City", Toast.LENGTH_LONG).show()
                } else if (!response.isSuccessful) {
                    Toast.makeText(context, response.code().toString() + " ", Toast.LENGTH_LONG).show()
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
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}
