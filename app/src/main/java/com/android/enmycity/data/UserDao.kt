package com.android.enmycity.data

import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class UserDao(val name: String = "",
                   val email: String = "",
                   val photoUrl: String = "",
                   val gender: Int = 0,
                   val birthday: String = "",
                   val creationDate: Date = Date(),
                   val alterDate: Date = Date(),
                   val statusId: Int = 0,
                   val coffeeLanguage: Boolean = false,
                   val nightLife: Boolean = false,
                   val localShopping: Boolean = false,
                   val gastronomicTour: Boolean = false,
                   val cityTour: Boolean = false,
                   val sportBreak: Boolean = false,
                   val volunteering: Boolean = false,
                   val location: GeoPoint = GeoPoint(0.0, 0.0),
                   val city: String = "",
                   val postalCode: Int = 0,
                   val countryCode: String = "",
                   val country: String = "",
                   val locality: String = "",
                   val subLocality: String = "",
                   val adminArea: String = "",
                   val subAdminArea: String = "")