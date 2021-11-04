package com.example.weatherappctn.presentation

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insetterNavigationBar()

        auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser
        if (currentuser != null) {
           // navigateToFinal()
        }

        login()
    }


    private fun login() {

        binding.loginButton.setOnClickListener {

            if (TextUtils.isEmpty(binding.usernameInput.text.toString())) {
                binding.usernameInput.error = "Please enter username"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.passwordInput.text.toString())) {
                binding.usernameInput.error = "Please enter password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(binding.usernameInput.text.toString(), binding.passwordInput.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        navigateToFinal()
                        Toast.makeText(context, "Te-ai logat! ", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }

        }

        binding.registerText.setOnClickListener {
            navigateToRegister()

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