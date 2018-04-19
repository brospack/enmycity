package com.android.enmycity.profile

class ProfilePresenter {
  private lateinit var view: ProfileView

  fun setView(profileView: ProfileView) {
    view = profileView
  }
}