package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName

data class GroupRequest(
    @SerializedName("name")var name: String,
    @SerializedName("user_id")var user_id: Int,
    @SerializedName("description")var description: String,
    )