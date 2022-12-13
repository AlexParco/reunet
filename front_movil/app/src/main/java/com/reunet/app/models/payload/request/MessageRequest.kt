package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MessageRequest(
    @SerializedName("user_id") var userId: String,
    @SerializedName("group_id") var groupId: String,
    @SerializedName("body") var bodyMessage: String
): Serializable
