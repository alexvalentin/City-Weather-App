package com.example.weatherappctn.presentation.onboardingscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappctn.databinding.FragmentOnBoardingImageBinding
import com.example.weatherappctn.model.OnBoardingPage

class ViewPagerAdapter(
    private val onBoardingPages : List<OnBoardingPage>
) : RecyclerView.Adapter<ViewPagerAdapter.OnBoardingPageViewHolder>() {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : OnBoardingPageViewHolder {
        val binding = FragmentOnBoardingImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder : OnBoardingPageViewHolder, position : Int) {
        val onBoardingPage = onBoardingPages[position]

        holder.binding.titleTv.setText(onBoardingPage.title)
        holder.binding.textDescriptionTv.setText(onBoardingPage.description)
        holder.binding.onBoardScreenIV.setImageResource(onBoardingPage.image)
    }

    override fun getItemCount() : Int = onBoardingPages.size

    inner class OnBoardingPageViewHolder(val binding : FragmentOnBoardingImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}