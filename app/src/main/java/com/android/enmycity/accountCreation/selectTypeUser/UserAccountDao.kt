package com.android.enmycity.accountCreation.selectTypeUser

import java.util.Date

data class UserAccountDao(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val photo: String = "",
    val type: Int = 0,
    val localId: String = "",
    val travellerId: String = "",
    val creationDate: Date = Date(),
    val alterDate: Date = Date(),
    val statusId: Int = 0
)