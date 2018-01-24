package com.android.enmycity

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.android.enmycity.interests.InterestsActivity
import com.android.enmycity.search.SearchActivity
import com.android.enmycity.user.LogoutUseCase
import com.android.enmycity.user.SelectTypeUserActivity
import com.android.enmycity.user.UserRepository
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.bt_facebook
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class UserLoginActivity : AppCompatActivity() {
  private val callbackManager = CallbackManager.Factory.create()
  private val firabaseAuthentication: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
  private val loginManager: LoginManager by lazy { LoginManager.getInstance() }
  private val userRepository: UserRepository by lazy { UserRepository(this) }
  private val logoutUseCase: LogoutUseCase by lazy { LogoutUseCase(firabaseAuthentication, loginManager) }

  override fun onCreate(savedInstanceState: Bundle?) {
    FacebookSdk.sdkInitialize(this)
    AppEventsLogger.activateApp(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    initFacebookButton()
  }

  override fun onStart() {
    super.onStart()

    if (firabaseAuthentication.currentUser != null) {
      when (userRepository.isUserCreated()) {
        true -> goToSearchActivity()
        false -> goToSelectTypeUserActivity()
      }
    }
  }

  private fun goToSelectTypeUserActivity() {
    finish()
    startActivity<SelectTypeUserActivity>()
  }

  private fun goToSearchActivity() {
    finish()
    startActivity<InterestsActivity>()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  private fun initFacebookButton() {
    bt_facebook.setReadPermissions("email", "public_profile", "user_birthday", "user_location", "location")
    val facebookCallback = object : FacebookCallback<LoginResult> {
      override fun onSuccess(loginResult: LoginResult) {
        loginFacebookUser()
      }

      override fun onCancel() {
      }

      override fun onError(error: FacebookException) {
      }
    }
    bt_facebook.registerCallback(callbackManager, facebookCallback)
  }

  private fun loginFacebookUser() {
    val accessToken = AccessToken.getCurrentAccessToken()
    val facebookCredential = FacebookAuthProvider.getCredential(accessToken.token)
    firabaseAuthentication.signInWithCredential(facebookCredential)
        .addOnCompleteListener {
          if (it.isSuccessful) {
            createUser(accessToken)
          }
        }
  }

  private fun createUser(accessToken: AccessToken) {
    val graphJsonCallback = GraphRequest.GraphJSONObjectCallback { jsonObject, _ ->
      run {
        with(UserRepository(this)) {
          saveUserId(jsonObject.getString("id"))
          saveUserName(jsonObject.getString("name"))
          saveUserEmail(jsonObject.getString("email"))
          saveUserGender(jsonObject.getString("gender"))
          saveUserBirthday(jsonObject.getString("birthday"))
          saveUserCity(jsonObject.getJSONObject("location").getString("name"))
          saveUserAvatar("http://graph.facebook.com/${jsonObject.getString("id")}/picture?type=large")
        }
        goToSelectTypeUserActivity()
      }
    }

    val graphRequest = GraphRequest.newMeRequest(accessToken, graphJsonCallback)

    val parameters = Bundle()
    parameters.putString("fields", "id,name,email,gender,birthday,location")
    graphRequest.parameters = parameters
    graphRequest.executeAsync()
  }
}
