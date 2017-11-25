package com.android.enmycity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.bt_facebook

class UserLoginActivity : AppCompatActivity() {
  private val callbackManager = CallbackManager.Factory.create()
  private lateinit var firebaseAuth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    FacebookSdk.sdkInitialize(this)
    AppEventsLogger.activateApp(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    firebaseAuth = FirebaseAuth.getInstance()
    initFacebookButton()
  }

  override fun onStart() {
    super.onStart()
    if (firebaseAuth.currentUser != null) {
      this.openInterestsActivity()
      finish()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  private fun initFacebookButton() {
    bt_facebook.setReadPermissions("email", "public_profile")
    val facebookCallback = object : FacebookCallback<LoginResult> {
      override fun onSuccess(loginResult: LoginResult) {
        loadUser(loginResult.accessToken)
      }

      override fun onCancel() {
      }

      override fun onError(error: FacebookException) {
      }
    }
    bt_facebook.registerCallback(callbackManager, facebookCallback)
  }

  private fun loadUser(accessToken: AccessToken) {
    val facebookCredential = FacebookAuthProvider.getCredential(accessToken.token)
    firebaseAuth.signInWithCredential(facebookCredential)
        .addOnCompleteListener {
          if (it.isSuccessful) {
            this.openInterestsActivity()
          }
        }
  }
}
