package com.example.weatherappctn.presentation

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
    }

    private fun goToLoginPage() {
        binding.gotoLoginTv.setOnClickListener {
            navigateToLogin()
        }
    }


    private fun register() {
        binding.registerButton.setOnClickListener {

            when {
                TextUtils.isEmpty(binding.emailSignUp.text.toString()) -> {
                    binding.emailSignUp.error = "Please enter first name "
                    return@setOnClickListener
                }
                TextUtils.isEmpty(binding.usernameSignUp.text.toString()) -> {
                    binding.emailSignUp.error = "Please enter user name "
                    return@setOnClickListener
                }
                TextUtils.isEmpty(binding.passwordSignUp.text.toString()) -> {
                    binding.emailSignUp.error = "Please enter password "
                    return@setOnClickListener
                }
                else -> auth.createUserWithEmailAndPassword(
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
            }
        }
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