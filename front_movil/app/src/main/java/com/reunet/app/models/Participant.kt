package com.reunet.app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Participant(
    @SerializedName("participant_id") var participantId: Int,
    @SerializedName("group_id") var groupId: Int,
    @SerializedName("user_id") var userId: Int,
    @SerializedName("created_at") var createdAt: String
): Serializable