package ayoria.chagua.reunetapp.api

import ayoria.chagua.reunetapp.models.RegisterResponse
import ayoria.chagua.reunetapp.models.LoginResponse
import ayoria.chagua.reunetapp.models.User
import retrofit2.Call
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST("register")
    @Headers("Authorization: Basic TUM6TUMxIU1D")
    fun registerUser(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("role") role: String
    ):Call<RegisterResponse>

    @FormUrlEncoded
    @Headers("Authorization: Basic TUM6TUMxIU1D")
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

}