package com.example.ping_now

data class MessageModel(
    var message: String? = "",
    var senderId: String? = "",
    var timeStamp: Long? = 0
)
