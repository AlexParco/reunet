package com.reunet.app.models

data class ChatGroup (
        var group: Group,
        var lastMessage: String,
        var dateLastMessage: String
    )