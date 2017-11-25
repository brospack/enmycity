package com.android.enmycity.user

data class FacebookUserModel(val id: Long,
                             val name: String,
                             val lastName: String,
                             val gender: GenderType,
                             val photoUrl: String,
                             val birthday: String,
                             val country: String,
                             val latitude: Float,
                             val longitude: Float,
                             val city: String)