package com.android.enmycity.chats

data class ChatDto(
    val status: Int = 0,
    val userType: Int = 0,
    val userUid: String = "",
    val ownerId: String = "",
    val guestId: String = "",
    val ownerPhoto: String = "",
    val ownerName: String = "",
    val guestPhoto: String = "",
    val guestName: String = ""
)