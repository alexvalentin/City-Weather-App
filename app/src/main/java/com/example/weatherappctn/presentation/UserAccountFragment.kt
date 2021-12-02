package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherappctn.databinding.FragmentUserAccountBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import com.example.weatherappctn.R
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

class UserAccountFragment : Fragment() {

    private lateinit var binding : FragmentUserAccountBinding
    private var auth : FirebaseAuth? = null

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentUserAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser

        if (auth!!.currentUser != null) {
            Toast.makeText(context, user?.email, Toast.LENGTH_SHORT).show()
            binding.displayAccountTv.text = "Hello,\n${user?.email}"
        }
    }

    private fun initListeners() {
        clickableSignOutBtn()
        clickableReturnToWeatherDisplayFragment()
        clickablePrivacyPolicy()
        changePassword()
        insetterNavigationBar()
    }

    private fun changePassword() {
        binding.changePasswordTv.setOnClickListener {
            navigateToResetPasswordFragment()
        }
    }

    private fun clickablePrivacyPolicy() {
        binding.privacyPolicyTv.setOnClickListener {
            showPrivacyPolicyDialog()
        }
    }

    private fun showPrivacyPolicyDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Privacy Policy")
        builder.setMessage(R.string.check_inbox)
        builder.setPositiveButton(R.string.prompt_ok) { _, _ -> }
        builder.show()
    }

    private fun clickableSignOutBtn() {
        binding.signOutBtn.setOnClickListener {
                auth!!.signOut()
                navigateLoginFragment()
        }
    }

    private fun clickableReturnToWeatherDisplayFragment() {
        binding.returnToWeatherDisplayIv.setOnClickListener {
            navigateToWeatherDisplayFragment()
        }
    }

    private fun navigateToResetPasswordFragment() {
        val action = UserAccountFragmentDirections.actionUserAccountFragmentToResetPasswordFragment()
        findNavController().navigate(action)
    }

    private fun navigateLoginFragment() {
        val action = UserAccountFragmentDirections.actionUserAccountFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToWeatherDisplayFragment() {
        val action = UserAccountFragmentDirections.actionUserAccountFragmentToWeatherDisplayFragment()
        findNavController().navigate(action)
    }

    private fun insetterNavigationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.titleHelloUserTv)
    }

}