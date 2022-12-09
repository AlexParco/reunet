package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Message(
    @SerializedName("message_id") var id: String,
    @SerializedName("user_id") var userId: String,
    @SerializedName("group_id") var groupId: String,
    @SerializedName("body") var body: String,
    @SerializedName("created_at") var createdAt: String
): Serializable