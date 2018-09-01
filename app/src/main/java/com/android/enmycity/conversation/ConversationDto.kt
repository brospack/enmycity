package com.android.enmycity.conversation

class ConversationDto(
    val ownerId: String = "",
    val ownerName: String = "",
    val ownerPhoto: String = "",
    val guestId: String = "",
    val guestName: String = "",
    val guestPhoto: String = "",
    val messages: List<Message> = listOf()
)