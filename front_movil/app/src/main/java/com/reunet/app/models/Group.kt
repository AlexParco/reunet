package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Group(
    @SerializedName("id") var id: String,
    @SerializedName("user_id") var userId: String,
    @SerializedName("description") var description: String,
    @SerializedName("name") var name: String,
    @SerializedName("created_at") var createdAt: String
): Serializable

