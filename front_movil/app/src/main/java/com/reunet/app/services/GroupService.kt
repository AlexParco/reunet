package com.reunet.app.services

import com.reunet.app.models.Activity
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.GroupRequest
import com.reunet.app.models.payload.response.ResponseApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface GroupService {

    @GET("group")
    @Headers("Content-Type: application/json")
    suspend fun getGroups():
             Response<ResponseApi<List<Group>>>

    @GET("activity")
    @Headers("Content-Type: application/json")
    suspend fun getActivityByGroupId(@Query("groupid") groupId: String):
            Response<ResponseApi<List<Activity>>>

    @GET("group/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getGroupById(@Path("id") id: Int):
            Response<ResponseApi<Group>>

    @POST("group")
    @Headers("Content-Type: application/json")
    suspend fun createGroup(@Body group: GroupRequest):
        Response<ResponseApi<Group>>

    @PUT("group/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateGroup(
        @Path("id")id: Int,
        @Body group: GroupRequest):
            Response<ResponseApi<Group>>

    @DELETE("group/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteGroup(@Path("id")id: Int):
            Response<ResponseApi<String>>

    companion object {
        fun build(token: String): GroupService{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.3:9999/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(ProvideToken().providesOkhttpClient(token))
                .build()
            return retrofit.create(GroupService::class.java)
        }
    }
}