package com.android.enmycity.search

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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.search_results_recyclerView
import kotlinx.android.synthetic.main.fragment_search.search_progressBar
import org.jetbrains.anko.longToast

class SearchFragment : Fragment(), SearchView {
  private val presenter: SearchPresenter by lazy { SearchPresenter(UserSharedPreferences(context!!), FirebaseFirestore.getInstance()) }
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_search, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
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