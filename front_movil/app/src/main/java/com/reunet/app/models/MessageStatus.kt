package com.reunet.app.models

data class MessageStatus(
    var message: Message,
    var isReport: Boolean,
    var isRemoved: Boolean
)
