package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherappctn.R
import com.example.weatherappctn.databinding.FragmentWeatherDisplayBinding

class WeatherDisplayFragment : Fragment() {

    private lateinit var binding : FragmentWeatherDisplayBinding
    //private val onBoardingPages = mutableListOf<OnBoardingPage>()

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentWeatherDisplayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}