package com.android.enmycity.search

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.MapUserDaoToUser
import com.android.enmycity.data.User
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.model.UserType
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Source

class SearchPresenter(
    private val userSharedPreferences: UserSharedPreferences,
    private val firestore: FirebaseFirestore
) {

  private lateinit var view: SearchView
  private var lastDocument: DocumentSnapshot? = null
  private val MAX_ELEMENTS = 2L
  private var profilesShowed = 0
  private val geoPointMin = GeoPoint(41.483409, 2.092803)
  private val geoPointMax = GeoPoint(41.497090, 2.059619)

  fun setView(view: SearchView) {
    this.view = view
  }

  fun onViewReady() {
    view.showProgressBar()
    if (userSharedPreferences.getLastSearch().isNotEmpty()) showLastSearch() else view.showPlaceAutocompleteEmpty()
  }

  fun onNewPagination() {
    profilesShowed = 0
    showLastSearch()
  }

  private fun showLastSearch() {
    userSharedPreferences.getLastSearch()
        .let {
          search(it.placeId, it.collection, it.location)
          view.showPlaceAutocompleteWithText(it.location)
        }
  }

  fun onPlaceSelected(placeId: String, location: String) {
    lastDocument = null
    profilesShowed = 0
    view.clearProfiles()
    when (userSharedPreferences.getUserLogged().userType) {
      UserType.TRAVELLER -> search(placeId, FirestoreCollectionNames.LOCALS, location)
      UserType.LOCAL -> search(placeId, FirestoreCollectionNames.TRAVELLERS, location)
    }
  }

  private fun search(placeId: String, collection: String, location: String) {
//    firestore.collection("geoTest")
//        .whereGreaterThan("position",geoPointMin)
//        .whereLessThan("position",geoPointMax)
//        .get(Source.SERVER)
//        .addOnSuccessListener {
//          it.documents.forEach {
//            val element = it["position"]
//          }
//        }
    val collectionReference = firestore.collection(collection)
    if (lastDocument == null) {
      collectionReference.whereEqualTo("placeId", placeId)
          .limit(MAX_ELEMENTS)
          .get(Source.SERVER)
          .addOnSuccessListener {
            saveLastSearch(placeId, collection, location)
            processDocuments(it.documents, collection)
            view.hideProgressBar()
          }
    } else {
      collectionReference.whereEqualTo("placeId", placeId)
          .startAfter(lastDocument!!)
          .limit(MAX_ELEMENTS)
          .get(Source.SERVER)
          .addOnSuccessListener {
            processDocuments(it.documents, collection)
            view.hideProgressBar()
          }
    }
  }

  private fun saveLastSearch(placeId: String, collectionName: String, location: String) {
    userSharedPreferences.saveLastSearch(Search(placeId, collectionName, location))
  }

  private fun processDocuments(documents: List<DocumentSnapshot>, collection: String) {
    documents.forEach {
      val userDao = it.toObject(UserDao::class.java)!!
      val user = MapUserDaoToUser().map(userDao, collection, it.id)
      if (userIsFiltered(user)) {
        view.addProfile(user)
        profilesShowed++
      }
      lastDocument = it
    }

    if (profilesShowed < MAX_ELEMENTS) {
      showLastSearch()
    }
  }

  private fun userIsFiltered(user: User): Boolean =
      with(userSharedPreferences.getUserLogged()) {
        when {
          user.cityTour == cityTour -> true
          user.coffeeLanguage == coffeeLanguage -> true
          user.gastronomicTour == gastronomicTour -> true
          user.localShopping == localShopping -> true
          user.nightLife == nightLife -> true
          user.sportBreak == sportBreak -> true
          user.volunteering == volunteering -> true
          else -> false
        }
      }
}