package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User (
    @SerializedName("id") var id: String,
    @SerializedName("firstname") var firstname: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("email") var email: String,
    @SerializedName("role") var role: String,
    @SerializedName("avatar") var avatar: String,
): Serializable
