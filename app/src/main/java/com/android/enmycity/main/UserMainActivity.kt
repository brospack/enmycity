package com.android.enmycity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View.VISIBLE
import com.android.enmycity.R
import com.android.enmycity.search.SearchFragment
import com.android.enmycity.user.UserFragment
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_user_main.search_results_bottomNavigationView
import kotlinx.android.synthetic.main.fragment_search.search_places_floatingActionButton
import org.jetbrains.anko.longToast

class UserMainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.bottomNavigationMenu_search -> replaceFragment(SearchFragment())
      R.id.bottomNavigationMenu_user -> replaceFragment(UserFragment())
      else -> true
    }
  }

  private fun replaceFragment(fragment: Fragment): Boolean {
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.myFragment, fragment)
      commit()
    }
    return true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_main)
    search_results_bottomNavigationView.setOnNavigationItemSelectedListener(this)
    replaceFragment(SearchFragment())
  }

//  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    search_places_floatingActionButton.visibility = VISIBLE
//    if (resultCode == Activity.RESULT_OK) {
//      if (requestCode == RESULT_CODE_PLACE_AUTOCOMPLETE) {
//        val place = PlacePicker.getPlace(this, data)
//        val placeName = place.name.toString()
//        val latitude = place.latLng.latitude.toString()
//        val longitude = place.latLng.longitude.toString()
//        longToast("Place: $placeName - $latitude; $longitude")
//      }
//    }
//  }
}