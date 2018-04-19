package com.android.enmycity.settings

import com.android.enmycity.data.UserDao

interface SettingsView {
  fun showUserData(userDao: UserDao)
  fun showLogoutMessage()
  fun goToLoginActivity()
}