package com.android.enmycity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.AccountCreationPreferences
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class UserLoginActivity : AppCompatActivity(), LoginView {
  private val callbackManager = CallbackManager.Factory.create()
  private val locationManager: LocationManager by lazy { this.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
  private val presenter: LoginPresenter by lazy {
    LoginPresenter(FirebaseAuth.getInstance(), AccountCreationPreferences(this),
        UserSharedPreferences(this))
  }
  private lateinit var googleSigInClient: GoogleSignInClient

  companion object {
    private const val REQUEST_CODE_SETTINGS = 1
    private const val REQUEST_CODE_GOOGLE = 9001
    private val PERMISSIONS = listOf("email", "public_profile")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    FacebookSdk.sdkInitialize(this)
    AppEventsLogger.activateApp(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    presenter.setView(this)
    presenter.onViewReady()
  }

  @SuppressLint("MissingPermission")
  override fun getLocation() {
    val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    if (location != null) {
      initFacebookButton()
      initGoogleButton()
      presenter.saveLocationInPreferences(location.latitude, location.longitude)
    } else {
      showsLocationsDialog()
    }
  }

  private fun showsLocationsDialog() {
    alert(R.string.userLogin_allow_location_message) {
      positiveButton(R.string.accept) { openSettings() }
      negativeButton(R.string.cancel) {
        showSnack(R.string.userlogin_open_settings_message,
            R.string.userlogin_open_settings_button)
        { openSettings() }
      }
    }.show()
  }

  private fun showSnack(message: Int, actionMessage: Int, onClickListener: (View) -> Unit) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionMessage, onClickListener)
        .show()
  }

  private fun openSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    startActivityForResult(intent, REQUEST_CODE_SETTINGS)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_SETTINGS) {
      presenter.onViewReady()
    } else if (requestCode == REQUEST_CODE_GOOGLE) {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      try {
        val account = task.getResult(ApiException::class.java)
        presenter.loginGoogleUser(account)
      } catch (e: ApiException) {
        toast(e.cause?.message.toString())
      }
    }
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  private fun initFacebookButton() {
    login_facebook_button.visibility = VISIBLE
    login_facebook_button.setReadPermissions(PERMISSIONS)

    val facebookCallback = object : FacebookCallback<LoginResult> {
      override fun onSuccess(loginResult: LoginResult) {
        login_facebook_button.visibility = GONE
        presenter.loginFacebookUser(AccessToken.getCurrentAccessToken())
      }

      override fun onCancel() {
      }

      override fun onError(error: FacebookException) {
      }
    }
    login_facebook_button.registerCallback(callbackManager, facebookCallback)
  }

  private fun initGoogleButton() {
    login_google_SignInButton.visibility = VISIBLE
    val googleSigInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.google_login_token))
        .requestEmail()
        .requestProfile()
        .build()
    googleSigInClient = GoogleSignIn.getClient(this, googleSigInOptions)

    login_google_SignInButton.setOnClickListener { signInWithGoogle() }
  }

  private fun signInWithGoogle() {
    startActivityForResult(googleSigInClient.signInIntent, REQUEST_CODE_GOOGLE)
  }

//  override fun createUser(accessToken: AccessToken) {
//    val graphJsonCallback = GraphRequest.GraphJSONObjectCallback { jsonObject, _ ->
//      run {
//        presenter.saveFacebookUserDataInPreferences(jsonObject)
//      }
//    }
//    GraphRequest.newMeRequest(accessToken, graphJsonCallback).apply {
//      parameters = Bundle().apply { putString("fields", "id,name,email,gender,birthday,location") }
//      executeAsync()
//    }
//  }

  override fun showProgressBar() {
    login_progress_bar.visibility = VISIBLE
  }

  override fun showLoginError(message: String) {
    toast(message)
  }

  override fun goToLoadUserTypeActivity() {
    openLoadUserTypeActivity()
  }

  override fun goToSelectUserTypeActivity() {
    openSelectUserTypeActivity()
  }

  override fun goToMainActivity() {
    openUserMainActivity()
  }
}
