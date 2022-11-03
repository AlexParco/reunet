package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class Activity(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("type") var typeActivity: String,
    @SerializedName("closed_at") var closedAt: Date
): Serializable
