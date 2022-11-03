package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ActivityRequest(
    @SerializedName("name") var name: String,
    @SerializedName("type") var type: String,
    @SerializedName("closed_at") var closed_at: String
)