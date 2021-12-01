package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherappctn.AuthenticationPatterns
import com.example.weatherappctn.AuthenticationPatterns.isFieldValid
import com.example.weatherappctn.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

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

        auth = FirebaseAuth.getInstance()

        binding.resetPasswordButton.setOnClickListener {


            when {
                binding.emailSignUp.text.toString().isFieldValid(AuthenticationPatterns.EMAIL_ADDRESS_PATTERN)
                -> {
                    auth!!.sendPasswordResetEmail(binding.emailSignUp.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                //navigateToFinal()

                                Toast.makeText(context, "Te-ai logged! ", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                            }
                        }

                }

            }

            /*!binding.emailSignUp.text.toString().isFieldValid(AuthenticationPatterns.EMAIL_ADDRESS_PATTERN) -> {
            binding.emailTextFieldSignUp.error = getString(R.string.error_email)
        } */


        }

    }


    //progressBar.setVisibility(View.VISIBLE);


}



