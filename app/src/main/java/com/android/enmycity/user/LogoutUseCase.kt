package com.android.enmycity.user

import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class LogoutUseCase(private val firebaseAuth: FirebaseAuth, private val loginManager: LoginManager) {
  fun logoutUser() {
    firebaseAuth.signOut()
    loginManager.logOut()
  }
}