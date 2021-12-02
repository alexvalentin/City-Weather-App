package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.weatherappctn.AuthenticationPatterns.EMAIL_ADDRESS_PATTERN
import com.example.weatherappctn.AuthenticationPatterns.isEmailValid
import com.example.weatherappctn.AuthenticationPatterns.isFieldValid
import com.example.weatherappctn.R
import com.example.weatherappctn.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding : FragmentForgotPasswordBinding
    private var auth : FirebaseAuth? = null

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        auth = FirebaseAuth.getInstance()

        binding.emailForgotPasswordEt.doAfterTextChanged {
            setResetPasswordButtonEnabledStatus()
            validateEmailAddress(binding.emailForgotPasswordEt.text.toString())
        }

        binding.resetPasswordButton.setOnClickListener {
            when {
                binding.emailForgotPasswordEt.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN)
                -> {
                   showPasswordResetRequestSuccessDialog()
                }
            }
        }
    }

    private fun initListeners() {
        insetterNavigationBar()
        clickableReturn()
    }

    private fun setResetPasswordButtonEnabledStatus() {
        val isResetPasswordButtonEnabled = binding.emailForgotPasswordEt.text.toString().isNotEmpty()
        binding.resetPasswordButton.isEnabled = isResetPasswordButtonEnabled
    }

    private fun validateEmailAddress(emailAddress : String) {
        if (emailAddress.isNotEmpty()) {
            if (isEmailValid(binding.emailForgotPasswordEt.text.toString())) {
                binding.emailForgotPasswordTil.isErrorEnabled = false
            }
        } else {
            binding.emailForgotPasswordTil.isErrorEnabled = false
        }
    }

    private fun showPasswordResetRequestSuccessDialog() {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(R.string.password_reset_link)
        builder.setMessage(R.string.check_inbox)

        builder.setPositiveButton(R.string.prompt_ok) { _, _ ->
            auth = FirebaseAuth.getInstance()
            auth!!.sendPasswordResetEmail(binding.emailForgotPasswordEt.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        navigateToLoginFragment()
                    } else {
                        Toast.makeText(context, "Something is wrong, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }
        }
        builder.show()
    }

    private fun clickableReturn() {
        binding.returnToLoginIv.setOnClickListener {
            navigateToLoginFragment()
        }
    }

    private fun navigateToLoginFragment() {
        val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun insetterNavigationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.titleForgotPasswordTv)
    }
}



