package com.android.enmycity.user

import com.android.enmycity.Constants
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.User
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.model.UserType
import com.google.firebase.firestore.FirebaseFirestore

class UserPresenter(private val userSharedPreferences: UserSharedPreferences) {
  private val firestore = FirebaseFirestore.getInstance()
  private lateinit var view: UserView

  fun setView(view: UserView) {
    this.view = view
  }

  fun onViewReady() = view.showUserData(userSharedPreferences.getUserLogged())

  fun onUserUpdated(userUpdated: User) {
    val documentId = userSharedPreferences.getUserLogged().id
    val collectionName = when (userSharedPreferences.getUserLogged().userType) {
      UserType.LOCAL -> FirestoreCollectionNames.LOCALS
      UserType.TRAVELLER -> FirestoreCollectionNames.TRAVELLERS
      else -> ""
    }
    val values = mutableMapOf<String, Any>(
        Constants.COFFEE_LANGUAGE to userUpdated.coffeeLanguage,
        Constants.LOCAL_SHOPPING to userUpdated.localShopping,
        Constants.VOLUNTEERING to userUpdated.volunteering,
        Constants.SPORT_BREAK to userUpdated.sportBreak,
        Constants.NIGHT_LIFE to userUpdated.nightLife,
        Constants.GASTRONOMIC_TOUR to userUpdated.gastronomicTour,
        Constants.CITY_TOUR to userUpdated.cityTour
    )

    firestore.collection(collectionName)
        .document(documentId)
        .update(values)
        .addOnSuccessListener { userSharedPreferences.saveUserLogged(userUpdated);view.showSuccessfulUpdateMessage() }
        .addOnFailureListener { view.showErrorUpdatingData(it.message ?: "") }
  }
}