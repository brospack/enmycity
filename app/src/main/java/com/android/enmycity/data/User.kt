package com.android.enmycity.data

import com.android.enmycity.user.model.GenderType
import com.android.enmycity.user.model.UserType
import com.google.firebase.firestore.GeoPoint

data class User(
    val uid: String = "",
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val userType: UserType = UserType.UNDEFINED,
    val photoUrl: String = "",
    val genderType: GenderType = GenderType.UNDEFINED,
    val birthday: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val coffeeLanguage: Boolean = false,
    val nightLife: Boolean = false,
    val localShopping: Boolean = false,
    val gastronomicTour: Boolean = false,
    val cityTour: Boolean = false,
    val sportBreak: Boolean = false,
    val volunteering: Boolean = false
)