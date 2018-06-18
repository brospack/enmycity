package com.android.enmycity.services

import android.content.Context
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.DeviceSharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.toast

class SaveTokenUseCase(private val context: Context) {
  private val deviceSharedPreferences: DeviceSharedPreferences by lazy { DeviceSharedPreferences(context) }
  private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

  fun saveToken() {
    FirebaseInstanceId.getInstance()?.token?.let {
      deviceSharedPreferences.saveToken(it)
      saveTokenInServer(it)
    }
  }

  private fun saveTokenInServer(token: String) {
    FirebaseAuth.getInstance().currentUser?.let {
      val userToken = mapOf("uid" to it.uid,
          "token" to token)
      firestore.collection(FirestoreCollectionNames.TOKENS)
          .add(userToken)
          .addOnFailureListener { context.toast("Error salvando token") }
    }
  }
}
