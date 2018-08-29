package com.android.enmycity.matches

data class ProposeViewModel(
    val chatId: String,
    val userId: String,
    val userName: String,
    val userPhoto: String,
    val isOwner: Boolean
)