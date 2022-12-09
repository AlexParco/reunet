package com.reunet.app.services

import com.reunet.app.models.payload.request.LoginRequest
import com.reunet.app.models.payload.request.RegisterRequest
import com.reunet.app.models.payload.response.LoginResponse
import com.reunet.app.models.payload.response.ResponseApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginService(@Body user: LoginRequest):
            Response<ResponseApi<LoginResponse>>


    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun registerService(@Body user: RegisterRequest):
            Response<ResponseApi<String>>

    companion object {
        fun build(): AuthService {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.3:9999/api/v1/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(AuthService::class.java)
        }
    }
}