package com.android.enmycity.accountCreation.selectTypeUser

interface SelectTypeUserView {
  fun showPlaceAutoocmpleteFragment()
  fun showUserData()
  fun showUserImage(photoUrl: String)
  fun showUserName(userName: String)
  fun showLocation(location: String)
  fun goToInterestsActivity()
  fun showError()
  fun showPlaceError()
}