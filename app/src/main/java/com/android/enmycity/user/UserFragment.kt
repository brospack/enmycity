package com.android.enmycity.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Switch
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user_profile.user_ProgressBar
import kotlinx.android.synthetic.main.fragment_user_profile.user_avatar_imageView
import kotlinx.android.synthetic.main.fragment_user_profile.user_cityTour_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_coffeeLanguage_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_gastronomicTour_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_localShopping_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_name_textView
import kotlinx.android.synthetic.main.fragment_user_profile.user_nightLife_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_save_floatingActionButton
import kotlinx.android.synthetic.main.fragment_user_profile.user_sportBreak_switch
import kotlinx.android.synthetic.main.fragment_user_profile.user_view
import kotlinx.android.synthetic.main.fragment_user_profile.user_volunteering_switch
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.toast

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
  private val presenter: UserPresenter by lazy { UserPresenter(UserSharedPreferences(context!!)) }
  private lateinit var lastInterests: String
  private lateinit var user: UserDao

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_user_profile, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
    user_save_floatingActionButton.setOnClickListener {
      val userUpdated = user.copy(
          coffeeLanguage = user_coffeeLanguage_switch.isChecked,
          nightLife = user_nightLife_switch.isChecked,
          localShopping = user_localShopping_switch.isChecked,
          gastronomicTour = user_gastronomicTour_switch.isChecked,
          cityTour = user_cityTour_switch.isChecked,
          sportBreak = user_sportBreak_switch.isChecked,
          volunteering = user_volunteering_switch.isChecked
      )
      presenter.onUserUpdated(userUpdated)
    }
  }

  override fun showUserData(userDao: UserDao) {
    user = userDao
    with(userDao) {
      showImage(photoUrl)
      user_name_textView.text = userDao.name
      user_coffeeLanguage_switch.isChecked = coffeeLanguage
      user_nightLife_switch.isChecked = nightLife
      user_localShopping_switch.isChecked = localShopping
      user_gastronomicTour_switch.isChecked = gastronomicTour
      user_cityTour_switch.isChecked = cityTour
      user_sportBreak_switch.isChecked = sportBreak
      user_volunteering_switch.isChecked = volunteering
    }

    lastInterests = getInterestsValues()

    user_view.childrenSequence().filter { it is Switch }
        .map { (it as Switch) }
        .iterator().forEach { it.setOnCheckedChangeListener { _, _ -> showSaveButton() } }

    user_ProgressBar.visibility = GONE
  }

  override fun showSuccessfulUpdateMessage() {
    activity?.toast(getString(R.string.user_updated_message))
  }

  override fun showErrorUpdatingData(message:String) {
    activity?.toast(message)
//    activity?.toast(getString(R.string.user_updated_failed_message))
  }

  private fun showImage(url: String) = Glide.with(context)
      .load(url)
      .into(user_avatar_imageView)

  private fun showSaveButton() = with(user_save_floatingActionButton) {
    visibility = if (!interestsAreChanged()) VISIBLE else GONE
  }

  private fun interestsAreChanged() = lastInterests == getInterestsValues()

  private fun getInterestsValues() = getSwitchValue(user_coffeeLanguage_switch) +
      getSwitchValue(user_nightLife_switch) +
      getSwitchValue(user_localShopping_switch) +
      getSwitchValue(user_gastronomicTour_switch) +
      getSwitchValue(user_cityTour_switch) +
      getSwitchValue(user_sportBreak_switch) +
      getSwitchValue(user_volunteering_switch)

  private fun getSwitchValue(switch: Switch) = if (switch.isChecked) "1" else "0"
}
