package com.android.enmycity.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.android.enmycity.R
import com.android.enmycity.matches.ProposeFragment
import com.android.enmycity.chats.ChatsFragment
import com.android.enmycity.search.SearchFragment
import com.android.enmycity.services.SaveTokenUseCase
import com.android.enmycity.settings.SettingsFragment
import com.android.enmycity.user.UserFragment
import kotlinx.android.synthetic.main.activity_user_main.search_results_bottomNavigationView

class UserMainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.bottomNavigationMenu_search -> replaceFragment(SearchFragment())
      R.id.bottomNavigationMenu_user -> replaceFragment(UserFragment())
      R.id.bottomNavigationMenu_matches -> replaceFragment(ProposeFragment())
      R.id.bottomNavigationMenu_messages -> replaceFragment(ChatsFragment())
      R.id.bottomNavigationMenu_settings -> replaceFragment(SettingsFragment())
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
    SaveTokenUseCase(this).saveToken()
    if (intent.extras != null) {
      val value = intent.extras?.getInt("hola", 0)
      if (value == 1) {
        replaceFragment(ProposeFragment())
      } else {
        replaceFragment(SearchFragment())
      }
    } else {
      replaceFragment(SearchFragment())
    }
    search_results_bottomNavigationView.setOnNavigationItemSelectedListener(this)
//    replaceFragment(SearchFragment())
//    val token = FirebaseInstanceId.getInstance().token
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
  }
}