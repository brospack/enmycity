package com.android.enmycity.interests

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Switch
import com.android.enmycity.R
import com.android.enmycity.search.SearchActivity
import com.android.enmycity.user.UserRepository
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
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
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.Date

class InterestsActivity : AppCompatActivity() {
  private val userRepository by lazy { UserRepository(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_interests)
    userRepository.let {
      showUserAvatar(it.getUserAvatar())
    }
    interests_next_floatingActionButton.setOnClickListener {
      when (thereAreAnyInterestChecked()) {
        true -> createFirebaseUser()
        false -> toast("ANDE VAS CABESTRO")
      }
    }

//    FirebaseAuth.getInstance().currentUser.let {
//      val email = it?.email ?: ""
//      val docRefer = firestore.collection("users").document(email)
//      docRefer.get().addOnCompleteListener {
//        if (it.isSuccessful) {
//        }
//      }
//    }
  }

  private fun showUserAvatar(url: String) = Glide.with(this).load(url).into(interests_user_image)

  private fun thereAreAnyInterestChecked() = getInterestsChecked() > 0

  private fun getInterestsChecked() = interests_view.childrenSequence().filter { it is Switch }
      .map { (it as Switch) }
      .filter { it.isChecked }
      .toList().size

  private fun createFirebaseUser() {
    val db = FirebaseFirestore.getInstance()
    val user = HashMap<String, Any>()
    val interests = HashMap<String, Any>()

    interests.apply {
      put("coffeeLanguage", interests_coffeeLanguage_switch.isChecked)
      put("nightLife", interests_nightLife_switch.isChecked)
      put("localShopping", interests_localShopping_switch.isChecked)
      put("gastronomicTour", interests_gastronomicTour_switch.isChecked)
      put("cityTour", interests_cityTour_switch.isChecked)
      put("sportBreak", interests_sportBreak_switch.isChecked)
      put("volunteering", interests_volunteering_switch.isChecked)
    }

    with(userRepository) {
      val userType = getUserType()
      user.apply {
        put("name", getUserName())
        put("email", getUserEmail())
        put("photoUrl", getUserAvatar())
        put("genderTypeId", getUserGender())
        put("birthday", getUserBirthday())
        put("city", getUserCity())
        put("alterDate", Date())
        put("creationDate", Date())
        put("statusId", 1)
        put("interests", interests)
      }

      db.collection(userType).document(getUserEmail())
          .set(user)
          .addOnSuccessListener { goToSearchActivity() }
          .addOnFailureListener { Log.i("SAVE", "FAIL") }
    }
  }

  private fun goToSearchActivity() {
    userRepository.setIsUserCreated(true)
    startActivity<SearchActivity>()
    finish()
  }
}