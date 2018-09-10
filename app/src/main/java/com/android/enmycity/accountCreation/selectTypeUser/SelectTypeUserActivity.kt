package com.android.enmycity.accountCreation.selectTypeUser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Switch
import com.android.enmycity.R
import com.android.enmycity.openInterestsActivity
import com.android.enmycity.user.AccountCreationPreferences
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_autocomplete_layout
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_avatar_imageView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_changeLocation_button
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_data_layout
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_local_switch
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_location_textView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_name_textView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_next_floatingActionButton
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_traveller_switch
import org.jetbrains.anko.toast

class SelectTypeUserActivity : AppCompatActivity(), SelectTypeUserView {
  override fun showPlaceError() {
    toast("Necesitas seleccionar una zona")
  }

  private val presenter by lazy { SelectTypeUserPresenter(AccountCreationPreferences(this)) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_type_user)
    presenter.setView(this)
    presenter.onViewReady()
  }

  override fun showPlaceAutoocmpleteFragment() {
    val autocompleteFragment = fragmentManager.findFragmentById(R.id.selectTypeUser_autocomplete_fragment) as PlaceAutocompleteFragment
    autocompleteFragment.apply {
      setFilter(AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build())
      setHint("Selecciona tu localidad")
      setOnPlaceSelectedListener(object : PlaceSelectionListener {
        override fun onPlaceSelected(place: Place?) {
          place?.let {
            presenter.onPlaceSelected(it)
            toast(it.id)
          }
        }

        override fun onError(p0: Status?) {
          TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
      })

    }
  }

  override fun showUserImage(photoUrl: String) {
    Glide.with(this)
        .load(photoUrl)
        .into(selectTypeUser_avatar_imageView)
  }

  override fun showUserName(userName: String) {
    selectTypeUser_name_textView.text = userName
  }

  override fun goToInterestsActivity() {
    openInterestsActivity()
  }

  override fun showError() {
  }

  override fun showLocation(location: String) {
    selectTypeUser_location_textView.text = location
    selectTypeUser_changeLocation_button.setOnClickListener { resetLocation() }
  }

  override fun showUserData() {
    selectTypeUser_autocomplete_layout.visibility = GONE
    selectTypeUser_data_layout.visibility = VISIBLE
    setListeners()
  }

  private fun resetLocation() {
    selectTypeUser_autocomplete_layout.visibility = VISIBLE
    selectTypeUser_data_layout.visibility = GONE
  }

  private fun toggle(switch: Switch, isChecked: Boolean) {
    switch.isChecked = !isChecked
  }

  private fun setListeners() {
    setUserLocalSwitchListener()
    setUserTravellerSwitchListener()
    setNextFloatingActionButtonListener()
  }

  private fun setUserLocalSwitchListener() {
    selectTypeUser_local_switch.setOnCheckedChangeListener { toggleView, isChecked ->
      when (toggleView) {
        selectTypeUser_local_switch -> toggle(selectTypeUser_traveller_switch, isChecked)
        selectTypeUser_traveller_switch -> toggle(selectTypeUser_local_switch, isChecked)
      }
    }
  }

  private fun setUserTravellerSwitchListener() {
    selectTypeUser_traveller_switch.setOnCheckedChangeListener { toggleView, isChecked ->
      when (toggleView) {
        selectTypeUser_local_switch -> toggle(selectTypeUser_traveller_switch, isChecked)
        selectTypeUser_traveller_switch -> toggle(selectTypeUser_local_switch, isChecked)
      }
    }
  }

  private fun setNextFloatingActionButtonListener() {
    selectTypeUser_next_floatingActionButton.setOnClickListener {
      when (selectTypeUser_local_switch.isChecked) {
        true -> presenter.onLocalCreated()
        false -> presenter.onTravellerCreated()
      }
    }
  }
}