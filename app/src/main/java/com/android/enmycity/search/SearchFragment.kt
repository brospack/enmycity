package com.android.enmycity.search

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.search_results_recyclerView
import kotlinx.android.synthetic.main.fragment_search.search_places_floatingActionButton
import kotlinx.android.synthetic.main.fragment_search.search_progressBar
import org.jetbrains.anko.longToast

class SearchFragment : Fragment(), SearchView {
  companion object {
    const val RESULT_CODE_PLACE_AUTOCOMPLETE = 10
  }

  private val presenter: SearchPresenter by lazy { SearchPresenter(UserSharedPreferences(context!!), FirebaseFirestore.getInstance()) }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_search, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
    search_places_floatingActionButton.setOnClickListener { startPlaceAutocomplete() }
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
      val place2 = PlacePicker.getPlace(context, data)
      val place = PlaceAutocomplete.getPlace(context, data)
      showLocation(place.latLng.latitude, place.latLng.longitude)
    }
  }

  override fun showMessage(message: String) {
    activity?.longToast(message)
  }

  override fun addProfiles(profiles: List<UserDao>) {
    search_results_recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(context, 2)
      adapter = ProfilesAdapter(profiles, context)
    }
  }

  override fun showLocation(latitude: Double, longitude: Double) {
  }

  override fun showProgressBar() {
    search_progressBar.visibility = VISIBLE
  }

  override fun hideProgressBar() {
    search_progressBar.visibility = GONE
  }
}