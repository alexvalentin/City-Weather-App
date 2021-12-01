package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.weatherappctn.AuthenticationPatterns.EMAIL_ADDRESS_PATTERN
import com.example.weatherappctn.AuthenticationPatterns.PASSWORD_PATTERN
import com.example.weatherappctn.AuthenticationPatterns.isEmailValid
import com.example.weatherappctn.AuthenticationPatterns.isFieldValid
import com.example.weatherappctn.AuthenticationPatterns.isPasswordValid
import com.example.weatherappctn.R
import com.example.weatherappctn.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        insetterNavigationBar()
        clickableStartWithOneHere()
        clickableForgotPassword()

        binding.emailLoginEt.setText("test1@test.ro")
        binding.passwordLoginEt.setText("test123A!")

        // Auth
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToFinal()
        }

        login()

        binding.emailLoginEt.doAfterTextChanged {
            setLoginButtonEnabledStatus()
            validateEmailAddress(binding.emailLoginEt.text.toString())
        }

        binding.passwordLoginEt.doAfterTextChanged {
            setLoginButtonEnabledStatus()
            validatePassword(binding.passwordLoginEt.text.toString())
        }
    }

    private fun clickableStartWithOneHere() {
        val spannableStringStartHere = SpannableString(binding.startHereTv.text)
        val clickableSpanLogHere : ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0 : View) {
                navigateToRegister()
            }

            override fun updateDrawState(ds : TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.WHITE
            }
        }

        spannableStringStartHere.setSpan(clickableSpanLogHere, 31, binding.startHereTv.text.length, Spanned.SPAN_INTERMEDIATE)
        binding.startHereTv.setText(spannableStringStartHere, TextView.BufferType.SPANNABLE)
        binding.startHereTv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun clickableForgotPassword() {
        val spannableStringFP = SpannableString(binding.forgotPasswordTv.text)
        val clickableSpanFP : ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0 : View) {
                navigateToForgotPassword()
            }

            override fun updateDrawState(ds : TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.WHITE
            }
        }

        spannableStringFP.setSpan(clickableSpanFP, 30, binding.forgotPasswordTv.text.length, Spanned.SPAN_INTERMEDIATE)
        binding.forgotPasswordTv.setText(spannableStringFP, TextView.BufferType.SPANNABLE)
        binding.forgotPasswordTv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun login() {

        binding.loginButton.setOnClickListener {


            when {
                binding.emailLoginEt.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) && binding.passwordLoginEt.text.toString()
                    .isFieldValid(PASSWORD_PATTERN)
                -> {
                    auth.signInWithEmailAndPassword(binding.emailLoginEt.text.toString(), binding.passwordLoginEt.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                navigateToFinal()

                                Toast.makeText(context, "You are logged! ", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                            }
                        }
                }

                !binding.emailLoginEt.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) && !binding.passwordLoginEt.text.toString()
                    .isFieldValid(PASSWORD_PATTERN)
                -> {
                    binding.emailLoginTil.error = getString(R.string.error_email)
                    binding.passwordLoginTil.error = getString(R.string.error_password)
                }

                !binding.emailLoginEt.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) -> {
                    binding.emailLoginTil.error = getString(R.string.error_email)
                }

                !binding.passwordLoginEt.text.toString().isFieldValid(PASSWORD_PATTERN) -> {
                    binding.passwordLoginTil.error = getString(R.string.error_password)
                }
            }


        }
    }

    private fun setLoginButtonEnabledStatus() {
        val isLoginButtonEnabled = binding.emailLoginEt.text.toString().isNotEmpty() && binding.passwordLoginEt.text.toString().isNotEmpty()
        binding.loginButton.isEnabled = isLoginButtonEnabled
    }

    private fun validateEmailAddress(emailAddress : String) {
        if (emailAddress.isNotEmpty()) {
            if (isEmailValid(binding.emailLoginEt.text.toString())) {
                binding.emailLoginTil.isErrorEnabled = false
            }
        } else {
            binding.emailLoginTil.isErrorEnabled = false
        }
    }

    private fun validatePassword(password : String) {
        binding.passwordLoginEt.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        if (password.isNotEmpty()) {


            if (isPasswordValid(binding.passwordLoginEt.text.toString())) {
                Toast.makeText(context, "Strong Password", Toast.LENGTH_SHORT).show()
                binding.passwordLoginTil.isErrorEnabled = false
            }

        } else {
            binding.passwordLoginTil.isErrorEnabled = false
            binding.passwordLoginEt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    private fun navigateToFinal() {
        val action = LoginFragmentDirections.actionLoginFragmentToWeatherDisplayFragment()
        findNavController().navigate(action)
    }

    private fun navigateToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun navigateToForgotPassword() {
        val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
        findNavController().navigate(action)
    }

    private fun insetterNavigationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.titleLoginTv)

    }
}