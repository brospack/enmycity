package com.android.enmycity.settings

import com.android.enmycity.data.User

interface SettingsView {
  fun showUserData(user: User)
  fun showLogoutMessage()
  fun goToLoginActivity()
}