package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName

data class GroupRequest(
    @SerializedName("activity_id")var activity_id: Int,
    @SerializedName("name")var name: String,
    @SerializedName("user_id")var user_id: Int
)