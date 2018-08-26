package com.android.enmycity.settings

import com.android.enmycity.data.UserLogged

interface SettingsView {
  fun showUserData(userLogged: UserLogged)
  fun showLogoutMessage()
  fun goToLoginActivity()
}