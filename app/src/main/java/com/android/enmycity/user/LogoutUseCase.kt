package com.android.enmycity.user

import com.android.enmycity.data.UserSharedPreferences
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class LogoutUseCase(private val userSharedPreferences: UserSharedPreferences) {
  fun logoutUser() {
    FirebaseAuth.getInstance().signOut()
    LoginManager.getInstance().logOut()
    userSharedPreferences.clear()
  }
}