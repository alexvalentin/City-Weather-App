package com.example.weatherappctn.presentation.onboardingscreen

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherappctn.R
import com.example.weatherappctn.databinding.FragmentOnBoardingScreenBinding
import com.example.weatherappctn.model.OnBoardingPage
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf
import androidx.navigation.fragment.findNavController

class OnBoardingScreen : Fragment() {

    private lateinit var binding : FragmentOnBoardingScreenBinding
    private val onBoardingPages = mutableListOf<OnBoardingPage>()

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentOnBoardingScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        postToList()
        setupViewPager()
        insetterNavigationBar()
        clickableSkipTv()
        clickableButtonNext()
    }

    private fun clickableSkipTv() {
        binding.skipTv.setOnClickListener {
            navigateToRegisterFragment()
        }
    }

    private fun clickableButtonNext() {
        binding.buttonNext.setOnClickListener {

            val lastPageIndex = binding.viewPager.adapter!!.itemCount - 2
            val currentItem = binding.viewPager.currentItem

            when {
                currentItem < lastPageIndex -> binding.viewPager.apply {
                    beginFakeDrag()
                    fakeDragBy(-800f)
                    endFakeDrag()
                    binding.buttonNext.text = requireContext().getString(R.string.next_button)
                    binding.viewPager.isUserInputEnabled = false

                }

                currentItem == lastPageIndex -> binding.viewPager.apply {
                    beginFakeDrag()
                    fakeDragBy(-800f)
                    endFakeDrag()
                    binding.buttonNext.text = requireContext().getString(R.string.on_boarding_last_page_button_text)
                    binding.skipTv.visibility = View.INVISIBLE
                    binding.buttonNext.setBackgroundResource(R.drawable.ic_pink_rectangle_get_started)

                    binding.indicatorSlide.setOnClickListener {
                        binding.viewPager.currentItem = binding.viewPager.currentItem - 1
                        binding.buttonNext.text = requireContext().getString(R.string.next_button)
                        binding.buttonNext.setBackgroundResource(R.drawable.ic_pink_rectangle)
                        binding.skipTv.visibility = View.VISIBLE
                        binding.indicatorSlide.setViewPager(binding.viewPager)
                    }
                }
                else -> navigateToRegisterFragment()
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(onBoardingPages)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicatorSlide.setViewPager(binding.viewPager)
    }

    private fun addToList(onBoardingPage : OnBoardingPage) {
        onBoardingPages.add(onBoardingPage)
    }

    private fun postToList() {
        onBoardingPages.clear()
        addToList(OnBoardingPage(R.string.prompt_safety, R.string.text_safety, R.drawable.icon))
        addToList(OnBoardingPage(R.string.prompt_scan, R.string.text_scan, R.drawable.icon))
        addToList(OnBoardingPage(R.string.prompt_ride, R.string.text_ride, R.drawable.icon))
        addToList(OnBoardingPage(R.string.prompt_parking, R.string.text_parking, R.drawable.icon))
        addToList(OnBoardingPage(R.string.prompt_rules, R.string.text_rules, R.drawable.icon))
    }

    private fun navigateToRegisterFragment() {
        val action = OnBoardingScreenDirections.actionOnBoardingScreenToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun insetterNavigationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.buttonNext)

        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.indicatorSlide)
    }
}


