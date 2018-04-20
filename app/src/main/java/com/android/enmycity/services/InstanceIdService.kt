package com.android.enmycity.services

import com.android.enmycity.data.DeviceSharedPreferences
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIdService : FirebaseInstanceIdService() {
  private val deviceSharedPreferences: DeviceSharedPreferences by lazy { DeviceSharedPreferences(this) }
  private val userSharedPreferences: UserSharedPreferences by lazy { UserSharedPreferences(this) }
  private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
  override fun onTokenRefresh() {
    super.onTokenRefresh()
    FirebaseInstanceId.getInstance()?.token?.let {
      deviceSharedPreferences.saveToken(it)
//      saveTokenInServer(it)
    }
  }

  private fun saveTokenInServer(token: String) {
    if (isUserLogged()) {
      val email = userSharedPreferences.getCurrentUser().email

      val documentReference = firestore.collection("devices").document(email)

      firestore.runTransaction {
        val snapshot = it.get(documentReference)

      }

    }
  }

  private fun isUserLogged() = FirebaseAuth.getInstance().currentUser != null
}