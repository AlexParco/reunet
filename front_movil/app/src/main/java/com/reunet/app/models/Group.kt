package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Group(
    @SerializedName("id") var id: String,
    @SerializedName("userId") var userId: String,
    @SerializedName("description") var description: String,
    @SerializedName("name") var name: String,
    @SerializedName("createdAt") var createdAt: String
): Serializable

