package com.android.enmycity.search

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.main.UserMainActivity
import com.android.enmycity.openUserMainActivity
import com.android.enmycity.user.UserFragment
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_search.myFragment
import kotlinx.android.synthetic.main.activity_search.search_progressBar
import kotlinx.android.synthetic.main.activity_search.search_results_bottomNavigationView
import kotlinx.android.synthetic.main.activity_search.search_results_recyclerView
import org.jetbrains.anko.longToast

class SearchActivity : AppCompatActivity(), SearchView, BottomNavigationView.OnNavigationItemSelectedListener {
  private fun replaceFragment(fragment: Fragment): Boolean {
    myFragment.visibility = VISIBLE
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.myFragment, fragment)
      commit()
    }
    return true
  }

  override fun showLocation(latitude: Double, longitude: Double) {
  }

  override fun showProgressBar() {
    search_progressBar.visibility = VISIBLE
  }

  override fun hideProgressBar() {
    search_progressBar.visibility = GONE
  }

  override fun addProfiles(profiles: List<UserDao>) {
    search_results_recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = GridLayoutManager(this@SearchActivity, 2)
      adapter = ProfilesAdapter(profiles, context)
    }
  }

  private val presenter: SearchPresenter by lazy { SearchPresenter(UserSharedPreferences(this), FirebaseFirestore.getInstance()) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search)
    search_results_bottomNavigationView.setOnNavigationItemSelectedListener(this)
    presenter.setView(this)
    presenter.onViewReady()
//    createUsers()
  }

  override fun showMessage(message: String) {
    longToast(message)
  }

  private fun createUsers() {
    val user = UserDao(
        name = "Angela Merkel",
        email = "testTraveller03@mail.com",
        photoUrl = "https://i.dawn.com/large/2016/11/582caa9db7dfa.jpg",
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

    FirebaseFirestore.getInstance()
        .collection("travellers")
        .document(user.email)
        .set(user)
        .addOnSuccessListener { }
        .addOnFailureListener { }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.bottomNavigationMenu_user -> replaceFragment(UserFragment())
      else -> true
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 999) {
        val place = PlacePicker.getPlace(this, data)
        val placeName = place.name.toString()
        val latitude = place.latLng.latitude.toString()
        val longitude = place.latLng.longitude.toString()
        longToast("Place: $placeName - $latitude; $longitude")
      }
    }
  }
}