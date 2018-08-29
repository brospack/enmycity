package com.android.enmycity.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.enmycity.R
import com.android.enmycity.data.User
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.openMainActivity
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.settings_avatar_imageView
import kotlinx.android.synthetic.main.fragment_settings.settings_logout_button
import org.jetbrains.anko.toast

class SettingsFragment : Fragment(), SettingsView {
  private val presenter: SettingsPresenter by lazy {
    SettingsPresenter(UserSharedPreferences(context!!), FirebaseAuth.getInstance(), LoginManager.getInstance())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_settings, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    FacebookSdk.sdkInitialize(activity?.baseContext)
    presenter.setView(this)
    presenter.onUserInfoLoad()
    settings_logout_button.setOnClickListener { presenter.onUserLogout() }
  }

  override fun showUserData(user: User) {
    Glide.with(this).load(user.photoUrl).into(settings_avatar_imageView)
  }

  override fun showLogoutMessage() {
    LoginManager.getInstance().logOut()
    activity?.toast("Bye bye")
  }

  override fun goToLoginActivity() {
    activity?.openMainActivity()
  }
}