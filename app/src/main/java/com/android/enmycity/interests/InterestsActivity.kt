package com.android.enmycity.interests

import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Switch
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.openUserMainActivity
import com.android.enmycity.services.SaveTokenUseCase
import com.android.enmycity.user.AccountCreationPreferences
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_interests.interests_cityTour_switch
import kotlinx.android.synthetic.main.activity_interests.interests_coffeeLanguage_switch
import kotlinx.android.synthetic.main.activity_interests.interests_gastronomicTour_switch
import kotlinx.android.synthetic.main.activity_interests.interests_localShopping_switch
import kotlinx.android.synthetic.main.activity_interests.interests_next_floatingActionButton
import kotlinx.android.synthetic.main.activity_interests.interests_nightLife_switch
import kotlinx.android.synthetic.main.activity_interests.interests_sportBreak_switch
import kotlinx.android.synthetic.main.activity_interests.interests_user_image
import kotlinx.android.synthetic.main.activity_interests.interests_view
import kotlinx.android.synthetic.main.activity_interests.interests_volunteering_switch
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.toast

class InterestsActivity : AppCompatActivity() {
  private val accountCreationPreferences by lazy { AccountCreationPreferences(this) }
  private val userPreferences by lazy { UserSharedPreferences(this) }
  private val saveTokenUseCase by lazy { SaveTokenUseCase(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_interests)
    accountCreationPreferences.let {
      showUserAvatar(it.getUserAvatar())
    }
    interests_next_floatingActionButton.setOnClickListener {
      when (thereAreAnyInterestChecked()) {
        true -> createFirebaseUser()
        false -> toast(R.string.interests_warning_selected)
      }
    }
  }

  private fun showUserAvatar(url: String) = Glide.with(this).load(url).into(interests_user_image)

  private fun thereAreAnyInterestChecked() = getInterestsChecked() > 0

  private fun getInterestsChecked() = interests_view.childrenSequence().filter { it is Switch }
      .map { (it as Switch) }
      .filter { it.isChecked }
      .toList().size

  private fun createFirebaseUser() {
    val addresses = Geocoder(this).getFromLocation(accountCreationPreferences.getLatitude(), accountCreationPreferences.getLongitude(), 1)

    with(accountCreationPreferences) {
      val user = UserDao(
          uid = getUserId(),
          name = getUserName(),
          email = getUserEmail(),
          photoUrl = getUserAvatar(),
          statusId = 1,
          coffeeLanguage = interests_coffeeLanguage_switch.isChecked,
          nightLife = interests_nightLife_switch.isChecked,
          localShopping = interests_localShopping_switch.isChecked,
          gastronomicTour = interests_gastronomicTour_switch.isChecked,
          cityTour = interests_cityTour_switch.isChecked,
          sportBreak = interests_sportBreak_switch.isChecked,
          volunteering = interests_volunteering_switch.isChecked,
          postalCode = addresses.first()?.postalCode?.toInt() ?: 0,
          location = GeoPoint(getLatitude(), getLongitude()),
          adminArea = addresses.first()?.adminArea ?: "",
          subAdminArea = addresses.first()?.subAdminArea ?: "")

      FirebaseFirestore.getInstance()
          .collection(getUserType())
          .add(user)
          .addOnSuccessListener { saveUserInPreferences(user); openUserMainActivity() }
          .addOnFailureListener { Log.i("Fail saving", it.message) }
    }
  }

  private fun saveUserInPreferences(user: UserDao) {
    when (accountCreationPreferences.getUserType() == "locals") {
      true -> userPreferences.saveUserLocal(user)
      false -> userPreferences.saveUserTraveller(user)
    }
    saveTokenUseCase.saveToken()
    accountCreationPreferences.clear()
  }
}