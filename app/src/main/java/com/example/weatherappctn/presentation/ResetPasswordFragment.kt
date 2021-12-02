package com.example.weatherappctn.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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

        auth = FirebaseAuth.getInstance()

        initListeners()
    }

    private fun initListeners() {
        updatePassword()
        clickableReturnBtn()
    }

    private fun updatePassword() {
        val user = FirebaseAuth.getInstance().currentUser

        binding.resetPasswordButton.setOnClickListener {
            user!!.updatePassword(binding.newPasswordEt.text.toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Password is updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update password!", Toast.LENGTH_SHORT).show()
                    }
                }
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