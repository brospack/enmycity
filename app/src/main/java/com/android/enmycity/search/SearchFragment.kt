package com.android.enmycity.search

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.enmycity.R
import com.android.enmycity.data.User
import com.android.enmycity.data.UserSharedPreferences
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.search_results_recyclerView
import kotlinx.android.synthetic.main.fragment_search.search_autocomplete_fragment
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class SearchFragment : Fragment(), SearchView {
  override fun showPlaceAutocompleteEmpty() {
    showPlaceAutocompleteFragment("Busca algo payasooooo")
  }

  override fun showPlaceAutocompleteWithText(location: String) {
    showPlaceAutocompleteFragment(location)
  }

  private val profilesAdapter: ProfilesAdapter = ProfilesAdapter(mutableListOf())
  private val presenter: SearchPresenter by lazy { SearchPresenter(UserSharedPreferences(context!!), FirebaseFirestore.getInstance()) }
  private val gridLayoutManager: GridLayoutManager by lazy { GridLayoutManager(context, 2) }
  private var placeAutocompleteFragment: PlaceAutocompleteFragment? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_search, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRecyclerView()
    presenter.setView(this)
    presenter.onViewReady()
  }

  private fun initRecyclerView() {
    search_results_recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = gridLayoutManager
      adapter = profilesAdapter
      addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)
          if (gridLayoutManager.findLastCompletelyVisibleItemPosition() >= gridLayoutManager.itemCount - 1) {
            presenter.onNewPagination()
          }
        }
      })
    }
  }

  private fun showPlaceAutocompleteFragment(location: String) {
    placeAutocompleteFragment =
        activity?.supportFragmentManager?.findFragmentById(R.id.search_autocomplete_fragment) as PlaceAutocompleteFragment?
    placeAutocompleteFragment?.apply {
      setFilter(AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build())
      setHint(location)
      setOnPlaceSelectedListener(object : PlaceSelectionListener {
        override fun onPlaceSelected(place: Place?) {
          place?.let {
            presenter.onPlaceSelected(it.id, it.name.toString())
          }
        }

        override fun onError(p0: Status?) {
          toast("error")
        }
      })

    }
  }

  override fun showMessage(message: String) {
    activity?.longToast(message)
  }

  override fun addProfile(user: User) {
    profilesAdapter.addUser(user)
    profilesAdapter.notifyDataSetChanged()
    hideProgressBar()
  }

  override fun clearProfiles() {
    profilesAdapter.clear()
    profilesAdapter.notifyDataSetChanged()
  }

  override fun showProgressBar() {
//    search_progressBar.visibility = VISIBLE
  }

  override fun hideProgressBar() {
//    search_progressBar.visibility = GONE
  }
}