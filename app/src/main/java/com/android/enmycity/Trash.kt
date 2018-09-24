package com.android.enmycity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import com.android.enmycity.accountCreation.selectTypeUser.UserAccountDao
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserDao
import com.android.enmycity.search.SearchFragment
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.fragment_search.search_places_floatingActionButton
import org.jetbrains.anko.longToast

private fun createUsers() {
  val db = FirebaseFirestore.getInstance()
  var batchTravellers = db.batch()
  var batchUsers = db.batch()

  for (i in 1..8) {
    val email = "testTraveller0$i@mail.com"
    val name = "traveller_$i"
    val photo = "https://i.dawn.com/large/2016/11/582caa9db7dfa.jpg"
    val uid = "xUiD-$i"
    val travelerId = "Tr-id-$i"

    val user = UserDao(
        uid = uid,
        name = name,
        email = email,
        photoUrl = photo,
        gender = 1,
        birthday = "17/07/1954",
        city = "Hamburgo",
        statusId = 1,
        location = GeoPoint(53.33, 10.00),
        coffeeLanguage = true,
        nightLife = true,
        localShopping = true,
        gastronomicTour = false,
        cityTour = false,
        sportBreak = false,
        volunteering = false)
    val documentReference = db.collection(FirestoreCollectionNames.TRAVELLERS).document(travelerId)
    batchTravellers.set(documentReference, user)

    val userAccound = UserAccountDao(
        uid = uid,
        name = name,
        email = email,
        photo = photo,
        type = 1,
        travellerId = travelerId
    )

    val userDocumentReference = db.collection(FirestoreCollectionNames.USERS).document(uid)
    batchUsers.set(userDocumentReference, userAccound)
  }
  batchTravellers.commit().addOnCompleteListener {
    batchUsers.commit()
  }
}

@SuppressLint("MissingPermission")
private fun getLocation() {
//  context?.let {
//    val googleApiClient = GoogleApiClient.Builder(it)
//        .addApi(Places.GEO_DATA_API)
//        .addApi(Places.PLACE_DETECTION_API)
//        .enableAutoManage(activity!!, this)
//        .build()
//
//    val result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null)
//    var resultPlaces = ""
//    result.setResultCallback {
//      it.forEach {
//        resultPlaces = "${it.place.id}-${it.place.name};  "
//
//      }
//      activity?.longToast(resultPlaces)
//    }
//  }
}
//
//private fun startPlaceAutocomplete() {
//  showProgressBar()
//  try {
//    search_places_floatingActionButton.visibility = View.GONE
//    val autocompleteFilters = AutocompleteFilter
//        .Builder()
//        .setTypeFilter(
//            AutocompleteFilter.TYPE_FILTER_CITIES)
//        .build()
//    val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//        .setFilter(autocompleteFilters)
//        .build(activity)
//
//    startActivityForResult(intent, SearchFragment.RESULT_CODE_PLACE_AUTOCOMPLETE)
//    hideProgressBar()
//  } catch (e: GooglePlayServicesRepairableException) {
//  } catch (e: GooglePlayServicesNotAvailableException) {
//  }
//}
//
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//  (requestCode == SearchFragment.RESULT_CODE_PLACE_AUTOCOMPLETE && resultCode == Activity.RESULT_OK).let {
//    val place = PlaceAutocomplete.getPlace(context, data)
////      showLocation(place.latLng.latitude, place.latLng.longitude)
//    presenter.onPlaceSelected(place.id)
//    search_places_floatingActionButton.visibility = View.VISIBLE
//  }
//}