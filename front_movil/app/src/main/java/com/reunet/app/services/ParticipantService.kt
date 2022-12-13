package com.reunet.app.services

import com.reunet.app.models.Participant
import com.reunet.app.models.payload.request.ParticipantRequest
import com.reunet.app.models.payload.response.ResponseApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ParticipantService {

    @GET("participants")
    @Headers("Content-Type: application/json")
    suspend fun getParticipantsByGroup(@Query("groupid") groupId: String):
            Response<ResponseApi<List<Participant>>>

    @POST("participants")
    @Headers("Content-Type: application/json")
    suspend fun addParticipant(@Body participant: ParticipantRequest):
            Response<ResponseApi<Participant>>


    @DELETE("participants/{id}")
    @Headers("Content-Type: application/json")
    suspend fun deleteParticipant(@Path("id")id: Int):
            Response<ResponseApi<String>>

    companion object {
        fun build(token: String): ParticipantService{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.3:9999/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(ProvideToken().providesOkhttpClient(token))
                .build()
            return retrofit.create(ParticipantService::class.java)
        }
    }
}