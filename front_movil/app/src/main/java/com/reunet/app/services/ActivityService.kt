package com.reunet.app.services

import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.models.payload.response.ResponseApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ActivityService {



    @GET("group/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getGroup(@Path("id")id: Int):
            Response<ResponseApi<Group>>

    @POST("activity")
    @Headers("Content-Type: application/json")
    suspend fun createActivity(@Body activity: ActivityRequest):
            Response<ResponseApi<Activity>>

    @PUT("activity/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateActivity(
        @Path("id")id: Int,
        @Body activity: ActivityRequest):
            Response<ResponseApi<Activity>>

    @DELETE("activity/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteActivity(@Path("id")id: Int):
            Response<ResponseApi<String>>

    companion object {
        fun build(token: String): ActivityService{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.3:9999/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(ProvideToken().providesOkhttpClient(token))
                .build()
            return retrofit.create(ActivityService::class.java)
        }
    }
}