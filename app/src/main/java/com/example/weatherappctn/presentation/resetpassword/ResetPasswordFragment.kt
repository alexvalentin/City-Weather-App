package com.example.weatherappctn.presentation.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.weatherappctn.AuthenticationPatterns.PASSWORD_PATTERN
import com.example.weatherappctn.AuthenticationPatterns.isFieldValid
import com.example.weatherappctn.AuthenticationPatterns.isPasswordValid
import com.example.weatherappctn.R
import com.example.weatherappctn.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {
    private lateinit var binding : FragmentResetPasswordBinding
    private var auth : FirebaseAuth? = null

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        auth = FirebaseAuth.getInstance()

        binding.newPasswordEt.doAfterTextChanged {
            setResetPasswordButtonEnabledStatus()
            validateNewPassword(binding.newPasswordEt.text.toString())
        }
    }

    private fun initListeners() {
        updatePassword()
        clickableReturnBtn()
    }

    private fun updatePassword() {

        val user = FirebaseAuth.getInstance().currentUser

        binding.resetPasswordButton.setOnClickListener {

            when {
                binding.newPasswordEt.text.toString().isFieldValid(PASSWORD_PATTERN) -> {

                    user!!.updatePassword(binding.newPasswordEt.text.toString().trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Password is updated!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to update password!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

                !binding.newPasswordEt.text.toString().isFieldValid(PASSWORD_PATTERN) -> {
                    binding.newPasswordTil.error = getString(R.string.error_password)
                }
            }
        }
    }

    private fun setResetPasswordButtonEnabledStatus() {
        val isResetPasswordButtonEnabled = binding.newPasswordEt.text.toString().isNotEmpty()
        binding.resetPasswordButton.isEnabled = isResetPasswordButtonEnabled
    }

    private fun validateNewPassword(newPassword : String) {
        if (newPassword.isNotEmpty()) {
            if (isPasswordValid(binding.newPasswordEt.text.toString())) {
                binding.newPasswordTil.isErrorEnabled = false
            }
        } else {
            binding.newPasswordTil.isErrorEnabled = false
        }
    }

    private fun clickableReturnBtn() {
        binding.returnToUserPageIv.setOnClickListener {
            navigateToUserAccountFragment()
        }
    }

    private fun navigateToUserAccountFragment() {
        val action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToUserAccountFragment()
        findNavController().navigate(action)
    }
}