package com.android.enmycity.interests

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class InterestsActivity : AppCompatActivity() {
  private lateinit var firebaseAuth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    if (currentUser != null) {
      val message = "hola ${currentUser.displayName}"
      toast(message)
    }
  }
}