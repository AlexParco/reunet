package ayoria.chagua.reunetapp.api

import ayoria.chagua.reunetapp.models.DefaultResponseRegister
import ayoria.chagua.reunetapp.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("role") role: String
    ):Call<DefaultResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>
}