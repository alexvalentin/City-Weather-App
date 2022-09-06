package com.example.weatherappctn

import android.annotation.SuppressLint
import android.content.Context
import java.util.regex.Pattern

@SuppressLint("StaticFieldLeak")
object AuthenticationPatterns {

    lateinit var context : Context

    val EMAIL_ADDRESS_PATTERN : Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    val PASSWORD_PATTERN : Pattern = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!?#$%^&+=])(?=\\S+$).{6,}$"
    )

    fun isEmailValid(email : String) = EMAIL_ADDRESS_PATTERN.matcher(email).matches()

    fun isPasswordValid(password : String) = PASSWORD_PATTERN.matcher(password).matches()

    fun String.isFieldValid(pattern : Pattern) = pattern.matcher(this).matches()

}