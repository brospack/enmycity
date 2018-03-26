package com.android.enmycity

import com.facebook.AccessToken

interface LoginView {
  fun getLocation()
  fun createUser(accessToken: AccessToken)
  fun goToMainActivity()
  fun goToSelectUserTypeActivity()
  fun goToLoadUserTypeActivity()
}