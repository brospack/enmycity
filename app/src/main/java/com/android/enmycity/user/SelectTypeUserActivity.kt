package com.android.enmycity.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.Switch
import com.android.enmycity.R
import com.android.enmycity.interests.InterestsActivity
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_avatar_imageView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_city_textView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_local_switch
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_name_textView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_next_floatingActionButton
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_traveller_switch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SelectTypeUserActivity : AppCompatActivity() {
  companion object {
    private val TEN_SECONDS = 10000L
    private val TWO_SECONDS = 2000L
  }

  private val userRepository by lazy { UserRepository(this) }
  private val locationRequest by lazy {
    LocationRequest().apply {
      priority = LocationRequest.PRIORITY_HIGH_ACCURACY
      interval = TEN_SECONDS
      fastestInterval = TWO_SECONDS
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_type_user)
    showUserData()
    setListeners()
    startLocation()
  }

  override fun onPause() {
    super.onPause()
  }

  private fun showUserData() {
    userRepository.let {
      showUserImage(it.getUserAvatar())
      showUserName(it.getUserName())
      showUserCity(it.getUserCity())
    }
  }

  private fun showUserImage(photoUrl: String) {
    Glide.with(this).load(photoUrl).into(selectTypeUser_avatar_imageView)
  }

  private fun showUserName(userName: String) {
    selectTypeUser_name_textView.text = userName
  }

  private fun showUserCity(city: String) {
    selectTypeUser_city_textView.text = city
  }

  private fun toggle(switch: Switch, isChecked: Boolean) {
    switch.isChecked = !isChecked
  }

  private fun setListeners() {
    setUserLocalSwitchListener()
    setUserTravellerSwithListener()
    setNextFloatinActionButtonListener()
  }

  private fun setUserLocalSwitchListener() {
    selectTypeUser_local_switch.setOnCheckedChangeListener { toggleView, isChecked ->
      when (toggleView) {
        selectTypeUser_local_switch -> toggle(selectTypeUser_traveller_switch, isChecked)
        selectTypeUser_traveller_switch -> toggle(selectTypeUser_local_switch, isChecked)
      }
    }
  }

  private fun setUserTravellerSwithListener() {
    selectTypeUser_traveller_switch.setOnCheckedChangeListener { toggleView, isChecked ->
      when (toggleView) {
        selectTypeUser_local_switch -> toggle(selectTypeUser_traveller_switch, isChecked)
        selectTypeUser_traveller_switch -> toggle(selectTypeUser_local_switch, isChecked)
      }
    }
  }

  private fun setNextFloatinActionButtonListener() {
    selectTypeUser_next_floatingActionButton.setOnClickListener {
      when (selectTypeUser_local_switch.isChecked) {
        true -> userRepository.setIsUserLocal(true)
        false -> userRepository.setIsUserLocal(false)
      }
      startActivity<InterestsActivity>()
    }
  }

  @SuppressLint("MissingPermission")
  private fun startLocation() {
    FusedLocationProviderClient(this).requestLocationUpdates(locationRequest,
        object : LocationCallback() {
          override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.let {
              showLocation(it)
            }
          }
        },
        Looper.myLooper())
  }

  private fun showLocation(locationResult: LocationResult) {
    with(locationResult.lastLocation) {
      toast("latitude: $latitude - longitude: $longitude")
    }
  }
}