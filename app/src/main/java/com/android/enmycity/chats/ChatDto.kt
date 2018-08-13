package com.android.enmycity.chats

data class ChatDto(
    val status: Int = 0,
    val userType: Int = 0,
    val ownerId: String = "",
    val travelerId: String = "",
    val localId: String = "",
    val ownerPhoto: String = "",
    val ownerName: String = "",
    val guestPhoto: String = "",
    val guestName: String = ""
)