package com.android.enmycity.search

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.android.enmycity.R
import com.android.enmycity.accountCreation.selectTypeUser.UserAccountDao
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.PlaceFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_search.search_results_recyclerView
import kotlinx.android.synthetic.main.fragment_search.search_places_floatingActionButton
import kotlinx.android.synthetic.main.fragment_search.search_progressBar
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class SearchFragment : Fragment(), SearchView, GoogleApiClient.OnConnectionFailedListener {
  override fun onConnectionFailed(p0: ConnectionResult) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    const val RESULT_CODE_PLACE_AUTOCOMPLETE = 10
  }

  private var count = 0
  private lateinit var rootView: View
  private lateinit var recyclerView: RecyclerView
  private val presenter: SearchPresenter by lazy { SearchPresenter(UserSharedPreferences(context!!), FirebaseFirestore.getInstance()) }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_search, container, false)
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
    search_places_floatingActionButton.setOnClickListener { startPlaceAutocomplete() }
    getLocation()
//    createUsers()
  }

  private fun startPlaceAutocomplete() {
    showProgressBar()
    try {
      val autocompleteFilters = AutocompleteFilter
          .Builder()
          .setTypeFilter(
              AutocompleteFilter.TYPE_FILTER_CITIES)
          .build()
      val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
          .setFilter(autocompleteFilters)
          .build(activity)

      startActivityForResult(intent, RESULT_CODE_PLACE_AUTOCOMPLETE)
      hideProgressBar()
      search_places_floatingActionButton.visibility = GONE
    } catch (e: GooglePlayServicesRepairableException) {
    } catch (e: GooglePlayServicesNotAvailableException) {
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    (requestCode == RESULT_CODE_PLACE_AUTOCOMPLETE && resultCode == RESULT_OK).let {
      val place = PlaceAutocomplete.getPlace(context, data)
//      showLocation(place.latLng.latitude, place.latLng.longitude)
      val placeData = place.viewport?.northeast.toString() + place.viewport?.southwest.toString()
      presenter.search(LatLngBounds(place.viewport?.southwest, place.viewport?.northeast))
      search_places_floatingActionButton.visibility = VISIBLE
//      activity?.longToast(placeData)
    }
  }

  override fun showMessage(message: String) {
    activity?.longToast(message)
  }

  override fun addProfiles(profiles: List<UserDao>) {
    recyclerView = rootView.findViewById(R.id.search_results_recyclerView)
    recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(context, 2)
      adapter = ProfilesAdapter(profiles, context)
      hideProgressBar()
    }
//    search_results_recyclerView.apply {
//      setHasFixedSize(true)
//      layoutManager = GridLayoutManager(context, 2)
//      adapter = ProfilesAdapter(profiles, context)
//      hideProgressBar()
////      activity?.toast(profiles.size.toString())
//    }
  }

  override fun showLocation(latitude: Double, longitude: Double) {
//    val query = "city"
//    val location = LatLng(latitude, longitude)
//    val latLng = LatLngBounds.Builder().include(location).include(location).build()
//
//    val filter = AutocompleteFilter.Builder()
//        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
//        .build()
//
//    val googleApiClient = GoogleApiClient.Builder(context!!)
//        .addApi(Places.GEO_DATA_API)
//        .addApi(Places.PLACE_DETECTION_API)
//        .enableAutoManage(activity!!, this)
//        .build()
//
//    val results = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, query, latLng, filter)
//    var resultData = ""
//    results.setResultCallback {
//      it.forEach {
//        resultData = it?.placeId ?: ""
//      }
//      activity?.longToast(resultData)
//    }
  }

  override fun showProgressBar() {
    search_progressBar.visibility = VISIBLE
  }

  override fun hideProgressBar() {
    search_progressBar.visibility = GONE
  }

  @SuppressLint("MissingPermission")
  private fun getLocation() {
//    context?.let {
//      val googleApiClient = GoogleApiClient.Builder(it)
//          .addApi(Places.GEO_DATA_API)
//          .addApi(Places.PLACE_DETECTION_API)
//          .enableAutoManage(activity!!, this)
//          .build()
//
//      val result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null)
//      var resultPlaces = ""
//      result.setResultCallback {
//        it.forEach {
//          resultPlaces = "${it.place.id}-${it.place.name};  "
//
//        }
//        activity?.longToast(resultPlaces)
//      }
//    }
  }

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
}