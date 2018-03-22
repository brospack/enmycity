package com.android.enmycity.user

import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore

class UserPresenter(private val userSharedPreferences: UserSharedPreferences,
                    private val firestore: FirebaseFirestore) {
  private lateinit var view: UserView

  fun setView(view: UserView) {
    this.view = view
  }

  fun onViewReady() {
    with(userSharedPreferences) {
      view.showUserData(getCurrentUser())
    }
  }
}