package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName

data class ParticipantRequest(
    @SerializedName("group_id") var email: String,
    @SerializedName("user_id") var password: String
)
