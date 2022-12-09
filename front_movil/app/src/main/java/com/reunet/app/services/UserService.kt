package com.reunet.app.services

import com.reunet.app.models.User
import com.reunet.app.models.payload.request.RegisterRequest
import com.reunet.app.models.payload.response.ResponseApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @GET("user/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getUserById(@Path("id")id: Int):
            Response<ResponseApi<User>>

    @PUT("user/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateUser(
        @Path("id")id: Int,
        @Body user: RegisterRequest):
            Response<ResponseApi<User>>

    companion object {
        fun build(token: String): UserService{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.3:9999/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(ProvideToken().providesOkhttpClient(token))
                .build()
            return retrofit.create(UserService::class.java)
        }
    }
}