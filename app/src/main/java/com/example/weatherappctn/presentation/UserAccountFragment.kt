package com.example.weatherappctn.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherappctn.databinding.FragmentUserAccountBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

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

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser

        if (auth!!.currentUser != null) {
            binding.titleTv.text = user?.email

        }
        binding.click.setOnClickListener {
            if (auth!!.currentUser != null) {
                auth!!.signOut()

                AuthStateListener { firebaseAuth ->
                    if (firebaseAuth.currentUser == null) {
                        auth?.signOut()

                    } else {
                    }
                }


                //navigateLogin()
                Toast.makeText(context, user?.email, Toast.LENGTH_SHORT).show()
                binding.titleTv.text = user?.email

            } else {
                Toast.makeText(context, "User does not exist!!", Toast.LENGTH_SHORT).show()
                binding.titleTv.text = user?.email.toString()
            }
        }


        /*user!!.updatePassword(newPassword.getText().toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Password is updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update password!", Toast.LENGTH_SHORT).show()
                    progressBar.setVisibility(View.GONE)
                }
            }*/

    }

    private fun navigateLogin() {
        val action = UserAccountFragmentDirections.actionUserAccountFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}