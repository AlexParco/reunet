package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("firstname") var firstname: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("email") var email: String,
    @SerializedName("role") var role: String,
    @SerializedName("password") var password: String
)
