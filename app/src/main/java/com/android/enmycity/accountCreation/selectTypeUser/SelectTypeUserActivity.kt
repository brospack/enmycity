package com.android.enmycity.accountCreation.selectTypeUser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Switch
import com.android.enmycity.R
import com.android.enmycity.openInterestsActivity
import com.android.enmycity.user.AccountCreationPreferences
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_avatar_imageView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_local_switch
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_name_textView
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_next_floatingActionButton
import kotlinx.android.synthetic.main.activity_select_type_user.selectTypeUser_traveller_switch

class SelectTypeUserActivity : AppCompatActivity(), SelectTypeUserView {
  private val presenter by lazy { SelectTypeUserPresenter(AccountCreationPreferences(this)) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_type_user)
    presenter.setView(this)
    presenter.onViewReady()
    setListeners()
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