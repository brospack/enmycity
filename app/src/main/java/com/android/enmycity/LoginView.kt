package com.android.enmycity

interface LoginView {
  fun getLocation()
  fun showLoginError(message: String)
  fun goToMainActivity()
  fun goToSelectUserTypeActivity()
  fun goToLoadUserTypeActivity()
  fun showProgressBar()
}