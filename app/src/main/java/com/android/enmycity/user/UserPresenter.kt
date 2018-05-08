package com.android.enmycity.user

import com.android.enmycity.Constants
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore

class UserPresenter(private val userSharedPreferences: UserSharedPreferences) {
  private val firestore = FirebaseFirestore.getInstance()
  private lateinit var view: UserView

  fun setView(view: UserView) {
    this.view = view
  }

  fun onViewReady() = view.showUserData(userSharedPreferences.getCurrentUser())

  fun onUserUpdated(userDao: UserDao) {
    val email = userSharedPreferences.getCurrentUser().email
    val userType = when (userSharedPreferences.getCurrentUserType()) {
      UserSharedPreferences.USER_TYPE_LOCAL -> "locals"
      UserSharedPreferences.USER_TYPE_TRAVELLER -> "travellers"
      else -> ""
    }
    val values = mutableMapOf<String, Any>(
        Constants.COFFEE_LANGUAGE to userDao.coffeeLanguage,
        Constants.LOCAL_SHOPPING to userDao.localShopping,
        Constants.VOLUNTEERING to userDao.volunteering,
        Constants.SPORT_BREAK to userDao.sportBreak,
        Constants.NIGHT_LIFE to userDao.nightLife,
        Constants.GASTRONOMIC_TOUR to userDao.gastronomicTour,
        Constants.CITY_TOUR to userDao.cityTour
    )

    firestore.collection(userType).document(email).update(values)
        .addOnSuccessListener { saveInPreferences(userDao);view.showSuccessfulUpdateMessage() }
        .addOnFailureListener { view.showErrorUpdatingData(it.message ?: "") }
  }

  private fun saveInPreferences(userDao: UserDao) {
    when (userSharedPreferences.getCurrentUserType()) {
      UserSharedPreferences.USER_TYPE_LOCAL -> userSharedPreferences.saveUserLocal(userDao)
      UserSharedPreferences.USER_TYPE_TRAVELLER -> userSharedPreferences.saveUserTraveller(userDao)
    }
  }
}