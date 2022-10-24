package com.reunet.app.models.payload.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("message") var message: String
)
