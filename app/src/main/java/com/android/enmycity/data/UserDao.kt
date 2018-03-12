package com.android.enmycity.data

import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class UserDao(val name: String = "",
                   val email: String = "",
                   val photoUrl: String = "",
                   val gender: Int = 0,
                   val birthday: String = "",
                   val city: String = "",
                   val creationDate: Date = Date(),
                   val alterDate: Date = Date(),
                   val statusId: Int = 0,
                   val location: GeoPoint = GeoPoint(0.0, 0.0),
                   val coffeeLanguage: Boolean = false,
                   val nightLife: Boolean = false,
                   val localShopping: Boolean = false,
                   val gastronomicTour: Boolean = false,
                   val cityTour: Boolean = false,
                   val sportBreak: Boolean = false,
                   val volunteering: Boolean = false)