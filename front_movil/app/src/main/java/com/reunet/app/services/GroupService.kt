package com.reunet.app.services

import com.google.android.datatransport.runtime.dagger.Provides
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.GroupRequest
import com.reunet.app.models.payload.response.GroupResponse
import com.reunet.app.models.payload.response.ResponseApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton


interface GroupService {
    @GET("group")
    @Headers("Content-Type: application/json")
    suspend fun getGroups():
            Response<ResponseApi<List<Group>>>

    @POST("group")
    @Headers("Content-Type: application/json")
    suspend fun createGroup(@Body group: GroupRequest):
        Response<ResponseApi<GroupResponse>>

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

        fun build(token: String): GroupService{
            val retrofit = Retrofit.Builder()
                .baseUrl("https://reunet-api.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(providesOkhttpClient(token))
                .build()
            return retrofit.create(GroupService::class.java)
        }
    }
}