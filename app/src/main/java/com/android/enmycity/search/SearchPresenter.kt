package com.android.enmycity.search

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.MapUserDaoToUser
import com.android.enmycity.data.User
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.model.UserType
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class SearchPresenter(
    private val userSharedPreferences: UserSharedPreferences,
    private val firestore: FirebaseFirestore
) {
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
  private val users = mutableSetOf<User>()
  private var coffeeLanguage = false
  private var localShopping = false
  private lateinit var lastDocument: DocumentSnapshot
  private var isPaginationActive = false

  fun setView(view: SearchView) {
    this.view = view
  }

  fun onViewReady() {
    view.showProgressBar()
    if (userSharedPreferences.getLastSearch().isNotEmpty()) {

    }
    when (userSharedPreferences.getUserLogged().userType) {
      UserType.TRAVELLER -> searchLocalProfiles()
      UserType.LOCAL -> searchTravellerProfiles()
    }
  }

  private fun getLastSearch() {
  }

  private fun doFirstSearch() {
  }

  fun search(bounds: LatLngBounds) {
    val northEast = GeoPoint(bounds.northeast.latitude, bounds.northeast.longitude)
    val southWest = GeoPoint(bounds.southwest.latitude, bounds.southwest.longitude)
    val travellersSearch = firestore.collection("searchTest")

    travellersSearch
        .whereLessThanOrEqualTo("latitude", northEast.latitude)
        .whereLessThanOrEqualTo("longitude", northEast.longitude)
        .whereGreaterThanOrEqualTo("latitude", southWest.latitude)
        .whereGreaterThanOrEqualTo("longitude", southWest.longitude)

        .get()
        .addOnCompleteListener {
          if (it.isSuccessful) {
            showElementsInView(it.result.documents, FirestoreCollectionNames.TRAVELLERS)
            coffeeLanguage = true
            getLocation()
          } else
            view.showMessage(it.exception?.message ?: "NOT SUCCESS")
        }
        .addOnFailureListener {
          view.showMessage(it.message ?: "ERROR")
        }

//    getInterestForFilter().forEach {
//      search.whereEqualTo(it, true)
//    }

//      search.whereLessThanOrEqualTo("latitude",bounds.northeast.latitude)
//      search.whereLessThanOrEqualTo("longitude",bounds.northeast.longitude)
//      search.whereGreaterThanOrEqualTo("latitude",bounds.southwest.latitude)
//      search.whereGreaterThanOrEqualTo("longitude",bounds.southwest.longitude)
  }

  private fun searchLocalProfiles() {
    val search = firestore.collection(FirestoreCollectionNames.LOCALS)
    getInterestForFilter().forEach {
      search.whereEqualTo(it, true)
    }
    search.get()
        .addOnCompleteListener {
          if (it.isSuccessful) {
            showElementsInView(it.result.documents, FirestoreCollectionNames.LOCALS)
          }
        }
    userSharedPreferences.getUserLogged().let {
      view.showMessage("You are local: ${it.name}")

    }
  }

  private fun searchTravellerProfiles() {
    val travellersSearch = firestore.collection(FirestoreCollectionNames.TRAVELLERS)

    if (!isPaginationActive) {
      travellersSearch.whereEqualTo("localShopping", true).limit(6).get().addOnSuccessListener {
        if (!it.isEmpty) {
          showElementsInView(it.documents, FirestoreCollectionNames.TRAVELLERS)
          lastDocument = it.documents[it.size() - 1]
          localShopping = true
          isPaginationActive = true
        }
      }
    } else {
      travellersSearch.whereEqualTo("localShopping", true).startAfter(lastDocument).limit(6).get().addOnSuccessListener {
        if (!it.isEmpty) {
          showElementsInView(it.documents, FirestoreCollectionNames.TRAVELLERS)
          lastDocument = it.documents[it.size() - 1]
          localShopping = true
        }
      }
    }
  }

  private fun searchProfiles(collectionName: String, placeId: String) {
    firestore.collection(collectionName)
        .whereEqualTo("placeId", placeId)
        .limit(4)

    userSharedPreferences.saveLastSearch(placeId)
  }

  private fun showElementsInView(documents: List<DocumentSnapshot>, collectionName: String) {
    documents.forEach {
      val userDao = it.toObject(UserDao::class.java)!!
      val user = MapUserDaoToUser().map(userDao, collectionName, it.id)
      view.addProfile(user)
    }
    view.hideProgressBar()
  }

  private fun getInterestForFilter() =
      with(userSharedPreferences.getUserLogged()) {
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

  private fun getLocation() {
    val latitude = userSharedPreferences.getUserLogged().location.latitude
    val longitude = userSharedPreferences.getUserLogged().location.longitude
//    view.showLocation(latitude, longitude)
//    view.addProfiles(users.toList())
    view.hideProgressBar()

    if (localShopping && coffeeLanguage) {
//      view.addProfiles(users.toList())
      view.hideProgressBar()
    }
  }
}