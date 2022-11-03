package com.reunet.app.models.payload.response

import com.google.gson.annotations.SerializedName
import com.reunet.app.models.Group

data class GroupResponse(
    @SerializedName("group") var group: Group
)
