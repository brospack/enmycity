package com.android.enmycity.data

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.user.model.GenderType
import com.android.enmycity.user.model.UserType

class MapUserLoggedFromUserDao() {
  fun map(userDao: UserDao, collectionName: String, userId: String): UserLogged {
    val userType = when (collectionName) {
      FirestoreCollectionNames.TRAVELLERS -> UserType.TRAVELLER
      FirestoreCollectionNames.LOCALS -> UserType.LOCAL
      else -> UserType.UNDEFINED
    }

    val genderType = when (userDao.gender) {
      1 -> GenderType.FEMALE
      2 -> GenderType.MALE
      else -> GenderType.UNDEFINED
    }
    return with(userDao) {
      UserLogged(uid = uid,
          id = userId,
          userType = userType,
          photoUrl = photoUrl,
          name = name,
          email = email,
          birthday = birthday,
          genderType = genderType,
          location = location,
          volunteering = volunteering,
          sportBreak = sportBreak,
          cityTour = cityTour,
          gastronomicTour = gastronomicTour,
          localShopping = localShopping,
          nightLife = nightLife,
          coffeeLanguage = coffeeLanguage)
    }
  }
}