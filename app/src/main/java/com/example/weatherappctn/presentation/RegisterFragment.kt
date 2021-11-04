package com.example.weatherappctn.presentation

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
import com.example.weatherappctn.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf

class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding

    private lateinit var auth : FirebaseAuth
    private var databaseReference : DatabaseReference? = null
    private var database : FirebaseDatabase? = null

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insetterNavigationBar()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()
        goToLoginPage()
        clickableLogInHere()

        binding.emailSignUp.doAfterTextChanged {
            setRegisterButtonEnabledStatus()
            validateEmailAddress(binding.emailSignUp.text.toString())
        }

        binding.usernameSignUp.doAfterTextChanged {
            setRegisterButtonEnabledStatus()
            validateUserName(binding.usernameSignUp.text.toString())
        }

        binding.passwordSignUp.doAfterTextChanged {
            setRegisterButtonEnabledStatus()
            validatePassword(binding.passwordSignUp.text.toString())
        }
    }

    private fun goToLoginPage() {
        binding.loginHereTv.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun setRegisterButtonEnabledStatus() {

        val isGetStartedButtonEnabled =  binding.emailSignUp.text.toString().isNotEmpty() &&
            binding.usernameSignUp.text.toString().isNotEmpty() &&
            binding.passwordSignUp.text.toString().isNotEmpty()

        binding.registerButton.isEnabled = isGetStartedButtonEnabled

    }

    private fun register() {
        binding.registerButton.setOnClickListener {

            when {
                binding.emailSignUp.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) && binding.passwordSignUp.text.toString()
                    .isFieldValid(PASSWORD_PATTERN) -> {
                    auth.createUserWithEmailAndPassword(
                        binding.emailSignUp.text.toString().trim(),
                        binding.passwordSignUp.text.toString().trim()
                    )
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val currentUser = auth.currentUser
                                val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                                currentUserDb?.child("username")?.setValue(binding.usernameSignUp.text.toString())

                                Toast.makeText(context, "Registration Success. ", Toast.LENGTH_LONG).show()
                                // navigateToLogin()
                                //navigateToWeather()

                            } else {
                                Toast.makeText(context, "Registration failed, please try again! ", Toast.LENGTH_LONG).show()
                            }
                        }
                    //saveData()
                    //retrieveData()
                    binding.emailTextFieldSignUp.error = null
                    binding.passwordTextFieldSignUp.error = null

                }

                !binding.emailSignUp.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) && !binding.passwordSignUp.text.toString()
                    .isFieldValid(
                        PASSWORD_PATTERN
                    ) -> {
                    binding.emailTextFieldSignUp.error = getString(R.string.error_email)
                    binding.passwordTextFieldSignUp.error = getString(R.string.error_password)
                }

                !binding.emailSignUp.text.toString().isFieldValid(EMAIL_ADDRESS_PATTERN) -> {
                    binding.emailTextFieldSignUp.error = getString(R.string.error_email)
                    binding.passwordTextFieldSignUp.error = null
                }

                !binding.passwordSignUp.text.toString().isFieldValid(PASSWORD_PATTERN) -> {
                    binding.passwordTextFieldSignUp.error = getString(R.string.error_password)
                    binding.emailTextFieldSignUp.error = null
                }

            }




        }
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
           /* binding.passwordSignUp.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                ResourcesCompat.getDrawable(resources, R.drawable.icon_password_selector, null),
                null
            ) */

            if (isPasswordValid(binding.passwordSignUp.text.toString())) {
                Toast.makeText(context, "Strong Password", Toast.LENGTH_SHORT).show()
                binding.passwordTextFieldSignUp.isErrorEnabled = false
            }

        } else {
            binding.passwordTextFieldSignUp.isErrorEnabled = false
            binding.passwordSignUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    private fun validateUserName(userName : String) {
        if (userName.isNotEmpty()) {

        } else if (userName.isEmpty()) {
            binding.usernameSignUp.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    private fun clickableLogInHere() {
        val spannableStringLogHere = SpannableString(binding.loginHereTv.text)
        val clickableSpanLogHere : ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0 : View) {
                goToLoginPage()
            }

            override fun updateDrawState(ds : TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.WHITE
            }
        }

        spannableStringLogHere.setSpan(clickableSpanLogHere, 37, 48, Spanned.SPAN_INTERMEDIATE)
        binding.loginHereTv.setText(spannableStringLogHere, TextView.BufferType.SPANNABLE)
        binding.loginHereTv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateToLogin() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToWeather() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToWeatherDisplayFragment()
        findNavController().navigate(action)
    }

    private fun insetterNavigationBar() {
        Insetter.builder()
            .marginBottom(insetType = windowInsetTypesOf(navigationBars = true))
            .applyToView(binding.titleTv)
    }
}