package com.android.enmycity.settings

import com.android.enmycity.data.UserSharedPreferences
import com.facebook.login.LoginManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

class SettingsPresenter(private val userSharedPreferences: UserSharedPreferences,
                        private val firebaseAuth: FirebaseAuth,
                        private val facebookLoginManager: LoginManager) {
  private lateinit var view: SettingsView

  fun setView(settingsView: SettingsView) {
    view = settingsView
  }

  fun onUserInfoLoad() {
    view.showUserData(userSharedPreferences.getCurrentUser())
  }

  fun onUserLogout() {
    facebookLoginManager.logOut()
    firebaseAuth.signOut()
    view.showLogoutMessage()
    view.goToLoginActivity()
  }
}