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

        binding.emailSignUp.setText("test1@test.ro")
        binding.passwordSignUp.setText("test123A!")


        auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser
        if (currentuser != null) {
           // navigateToFinal()
        }

        login()

        binding.emailSignUp.doAfterTextChanged {
            setLoginButtonEnabledStatus()
            validateEmailAddress(binding.emailSignUp.text.toString())
        }

        binding.passwordSignUp.doAfterTextChanged {
            setLoginButtonEnabledStatus()
            validatePassword(binding.passwordSignUp.text.toString())
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


    private fun login() {

        binding.loginButton.setOnClickListener {


            when {
                binding.emailSignUp.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) && binding.passwordSignUp.text.toString()
                    .isFieldValid(PASSWORD_PATTERN)
                -> {
                    auth.signInWithEmailAndPassword(binding.emailSignUp.text.toString(), binding.passwordSignUp.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                navigateToFinal()

                                Toast.makeText(context, "Te-ai logat! ", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                            }
                        }

                }

                !binding.emailSignUp.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) && !binding.passwordSignUp.text.toString()
                    .isFieldValid(PASSWORD_PATTERN)
                -> {
                    binding.emailTextFieldSignUp.error = getString(R.string.error_email)
                    binding.passwordTextFieldSignUp.error = getString(R.string.error_password)
                }

                !binding.emailSignUp.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) -> {
                    binding.emailTextFieldSignUp.error = getString(R.string.error_email)
                }

                !binding.passwordSignUp.text.toString().isFieldValid(PASSWORD_PATTERN) -> {
                    binding.passwordTextFieldSignUp.error = getString(R.string.error_password)
                }
            }


        }
    }

    private fun setLoginButtonEnabledStatus() {
        val isLoginButtonEnabled = binding.emailSignUp.text.toString().isNotEmpty() && binding.passwordSignUp.text.toString().isNotEmpty()
        binding.loginButton.isEnabled = isLoginButtonEnabled
    }

    private fun validateEmailAddress(emailAddress : String) {
        if (emailAddress.isNotEmpty()) {
            if (isEmailValid(binding.emailSignUp.text.toString())) {
                binding.emailTextFieldSignUp.isErrorEnabled = false
            }
        } else {
            binding.emailTextFieldSignUp.isErrorEnabled = false
        }
    }

    private fun validatePassword(password : String) {
        binding.passwordSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        if (password.isNotEmpty()) {


            if (isPasswordValid(binding.passwordSignUp.text.toString())) {
                Toast.makeText(context, "Strong Password", Toast.LENGTH_SHORT).show()
                binding.passwordTextFieldSignUp.isErrorEnabled = false
            }

        } else {
            binding.passwordTextFieldSignUp.isErrorEnabled = false
            binding.passwordSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
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

    private fun insetterNavigationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.titleTv)

    }

}