package com.reunet.app.services

import com.google.android.datatransport.runtime.dagger.Provides
import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.ActivityRequest
import com.reunet.app.models.payload.response.ResponseApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.inject.Singleton

interface ActivityService {

    @GET("activity")
    @Headers("Content-Type: application/json")
    suspend fun getActivities():
            Response<ResponseApi<List<Activity>>>

    @GET("activity/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getActivity(@Path("id")id: Int):
            Response<ResponseApi<Activity>>

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
        @Provides
        @Singleton
        fun providesOkhttpClient(token:String): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder().addInterceptor(AuthInterceptor("Bearer",token )
            ).addInterceptor(interceptor)
                .build()
        }

        fun build(token: String): ActivityService{
            val retrofit = Retrofit.Builder()
                .baseUrl("https://reunet-api.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(providesOkhttpClient(token))
                .build()
            return retrofit.create(ActivityService::class.java)
        }
    }
}