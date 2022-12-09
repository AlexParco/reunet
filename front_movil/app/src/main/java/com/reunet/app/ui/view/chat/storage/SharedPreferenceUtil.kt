package com.reunet.app.ui.view.chat.storage

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.reunet.app.models.User
import com.reunet.app.models.payload.request.RegisterRequest
import java.io.File

class SharedPreferenceUtil {
    companion object {
        private const val SHARED_PREFERENCE_KEY = "SHARED_PREFERENCE_KEY"
        private lateinit var sharedPreference: SharedPreferences
        private const val TOKEN = "TOKEN"
        private const val USER = "USER_KEY"
    }

    fun setSharedPreference(context: Context){
        sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String){
        sharedPreference
            .edit()
            .putString(TOKEN, token)
            .apply()
    }

    fun saveUser(user: User){
        val gson = Gson()
        val jsonUser = gson.toJson(user)
        sharedPreference
            .edit()
            .putString(USER, jsonUser)
            .apply()
    }

    fun getUser(): User?{
        val gson = Gson()
        val user : User?
        val jsonUser = sharedPreference.getString(USER, "")
        user = gson.fromJson(jsonUser, User::class.java)
        return user
    }

    fun getToken(): String? {
        val token = sharedPreference.getString(TOKEN, "")
        return token
    }
}