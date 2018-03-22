package com.android.enmycity.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_user_profile.user_avatar_imageView
import kotlinx.android.synthetic.main.fragment_user_profile.user_cityTour_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_coffeeLanguage_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_gastronomicTour_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_localShopping_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_name_textView
import kotlinx.android.synthetic.main.fragment_user_profile.user_nightLife_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_sportBreak_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_volunteering_switch

class UserFragment : Fragment(), UserView {
  /*
  countryCode -> ES
  countryName -> España
  adminArea -> Cataluña
  subAdminArea -> Barcelona
  locality -> Sant Cugat del Valles
  featureName -> 129
  subLocality ->
  thoroughfare -> Avinguda de la via augusta
  subThoroughfare-> 129
  url ->
   */
  private val presenter: UserPresenter by lazy { UserPresenter(UserSharedPreferences(context!!), FirebaseFirestore.getInstance()) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_user_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
    val userSharedPreferences = UserSharedPreferences(view.context)
//    val latitude = userSharedPreferences.getCurrentUser().location.latitude
//    val longitude = userSharedPreferences.getCurrentUser().location.longitude
//
//    val geocoder = Geocoder(view.context)
//    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
//
//    addresses.first()?.let {
//      userProfile_name_TextView.text = userSharedPreferences.getCurrentUser().name
//      userProfile_country_TextView.text = it.countryName ?: ""
//      userProfile_city_TextView.text = it.adminArea ?: ""
//      userProfile_zipCode_TextView.text = it.postalCode ?: ""
//      userProfile_location_TextView.text = it.locality ?: ""
//    }
//
//    button2.setOnClickListener {
//      initPlacePicker();
//    }
  }

  //  private fun initPlacePicker() {
//    val builder = PlacePicker.IntentBuilder()
//    activity?.startActivityForResult(builder.build(activity), 999)
//  }
  override fun showUserData(userDao: UserDao) {
    with(userDao) {
      showImage(photoUrl)
      user_name_textView.text = name
      user_coffeeLanguage_switch.isChecked = coffeeLanguage
      user_nightLife_switch.isChecked = nightLife
      user_localShopping_switch.isChecked = localShopping
      user_gastronomicTour_switch.isChecked = gastronomicTour
      user_cityTour_switch.isChecked = cityTour
      user_sportBreak_switch.isChecked = sportBreak
      user_volunteering_switch.isChecked = volunteering
    }
  }

  private fun showImage(url: String) {
    Glide.with(context).load(url).into(user_avatar_imageView)
  }
}