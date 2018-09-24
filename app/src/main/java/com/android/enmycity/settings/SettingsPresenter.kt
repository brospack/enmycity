package com.android.enmycity.settings

import com.android.enmycity.data.UserSharedPreferences
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class SettingsPresenter(
    private val userSharedPreferences: UserSharedPreferences,
    private val firebaseAuth: FirebaseAuth,
    private val facebookLoginManager: LoginManager
) {
  private lateinit var view: SettingsView

  fun setView(settingsView: SettingsView) {
    view = settingsView
  }

  fun onUserInfoLoad() {
    view.showUserData(userSharedPreferences.getUserLogged())
  }

  fun onUserLogout() {
    facebookLoginManager.logOut()
    firebaseAuth.signOut()
    userSharedPreferences.clear()
    view.showLogoutMessage()
    view.goToLoginActivity()
  }
}