package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Group(
    @SerializedName("id") var id: String,
    @SerializedName("userId") var userId: String,
    @SerializedName("activityId") var activityId: String,
    @SerializedName("name") var name: String
): Serializable

