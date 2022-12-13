package com.reunet.app.services

import com.reunet.app.models.Message
import com.reunet.app.models.payload.request.MessageRequest
import com.reunet.app.models.payload.response.ResponseApi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageService {

    @GET("message")
    @Headers("Content-Type: application/json")
    suspend fun getMessages(@Query("groupid") groupId: String):
            Response<ResponseApi<List<Message>>>

    @POST("message")
    @Headers("Content-Type: application/json")
    suspend fun sendMessage(@Body message: MessageRequest):
            Response<ResponseApi<Message>>


    companion object {
        fun build(token: String): MessageService{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.3:9999/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(ProvideToken().providesOkhttpClient(token))
                .build()

            return retrofit.create(MessageService::class.java)
        }
    }
}