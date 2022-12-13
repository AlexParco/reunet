package com.reunet.app.models.payload.response

import com.google.gson.annotations.SerializedName
import com.reunet.app.models.User
import java.io.Serializable

data class LoginResponse(
    @SerializedName("user") var user: User,
    @SerializedName("token") var token: String
)
