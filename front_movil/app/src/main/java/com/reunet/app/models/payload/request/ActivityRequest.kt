package com.reunet.app.models.payload.request

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ActivityRequest(
    @SerializedName("closed_at") var closed_at: Date,
    @SerializedName("name") var name: String,
    @SerializedName("typeActivity") var type: String
)