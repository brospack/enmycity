package com.android.enmycity.accountCreation.selectTypeUser

interface SelectTypeUserView {
  fun showUserImage(photoUrl: String)
  fun showUserName(userName: String)
  fun goToInterestsActivity()
  fun showError()
}