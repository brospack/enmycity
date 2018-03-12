package com.android.enmycity.search

import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SearchPresenter(private val userSharedPreferences: UserSharedPreferences,
                      private val firestore: FirebaseFirestore) {
  companion object {
    private const val CITY_TOUR = "cityTour"
    private const val COFFEE_LANGUAGE = "coffeeLanguage"
    private const val GASTRONOMIC_TOUR = "gastronomicTour"
    private const val LOCAL_SHOPPING = "localShopping"
    private const val NIGHT_LIFE = "nightLife"
    private const val SPORT_BREAK = "sportBreak"
    private const val VOLUNTEERING = "volunteering"
  }

  private lateinit var view: SearchView

  fun setView(view: SearchView) {
    this.view = view
  }

  fun onViewReady() {
    when (userSharedPreferences.getCurrentUserType()) {
      UserSharedPreferences.USER_TYPE_TRAVELLER -> searchLocalProfiles()
      UserSharedPreferences.USER_TYPE_LOCAL -> searchTravellerProfiles()
    }
  }

  private fun searchLocalProfiles() {
    val search = firestore.collection("locals")
    getInterestForFilter().forEach {
      search.whereEqualTo(it, true)
    }
    search.get()
        .addOnCompleteListener {
          if (it.isSuccessful) {
            showElementsInView(it.result.documents)
          }
        }
    userSharedPreferences.getUserLocal().let {
      view.showMessage("You are local: ${it.name}")

    }
  }

  private fun showElementsInView(documents: List<DocumentSnapshot>) {
    val users = mutableListOf<UserDao>()
    documents.forEach { users.add(it.toObject(UserDao::class.java)) }
    view.addProfiles(users)
    view.hideProgressBar()
  }

  private fun searchTravellerProfiles() {
    val search = firestore.collection("travellers")
    getInterestForFilter().forEach {
      search.whereEqualTo(it, true)
    }
    search.get()
        .addOnCompleteListener {
          if (it.isSuccessful) {
            showElementsInView(it.result.documents)
          }
        }
  }

  private fun getInterestForFilter() =
      with(userSharedPreferences.getCurrentUser()) {
        val interests = mutableListOf<String>()
        if (coffeeLanguage) interests.add(COFFEE_LANGUAGE)
        if (cityTour) interests.add(CITY_TOUR)
        if (gastronomicTour) interests.add(GASTRONOMIC_TOUR)
        if (localShopping) interests.add(LOCAL_SHOPPING)
        if (nightLife) interests.add(NIGHT_LIFE)
        if (sportBreak) interests.add(SPORT_BREAK)
        if (volunteering) interests.add(VOLUNTEERING)
        interests
      }


}