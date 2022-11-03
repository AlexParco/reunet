package com.reunet.app.storage

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.reunet.app.models.payload.request.RegisterRequest
import java.io.File

class SharedPreferenceUtil {
    companion object {
        private const val SHARED_PREFERENCE_KEY = "SHARED_PREFERENCE_KEY"
        private lateinit var sharedPreference: SharedPreferences
        private const val USER = "USER_KEY"
    }

    fun setSharedPreference(context: Context){
        sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun saveUser(user: RegisterRequest){
        val gson = Gson()
        val jsonUser = gson.toJson(user)
        sharedPreference
            .edit()
            .putString(USER, jsonUser)
            .apply()
    }

    fun getUser(): RegisterRequest?{
        val gson = Gson()
        var user: RegisterRequest? = null
        val jsonUser = sharedPreference.getString(USER, "")
        user = gson.fromJson(jsonUser, RegisterRequest::class.java)
        return user
    }
}