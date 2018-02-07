package com.android.enmycity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.View
import android.view.View.VISIBLE
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_login.login_facebook_button
import kotlinx.android.synthetic.main.activity_login.login_progress_bar
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class UserLoginActivity : AppCompatActivity() {
  private val callbackManager = CallbackManager.Factory.create()
  private val firabaseAuthentication: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
  private val loginManager: LoginManager by lazy { LoginManager.getInstance() }
  private val userRepository: UserRepository by lazy { UserRepository(this) }
  private val logoutUseCase: LogoutUseCase by lazy { LogoutUseCase(firabaseAuthentication, loginManager) }
  private val locationManager: LocationManager by lazy { this.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

  companion object {
    private const val REQUEST_CODE_SETTINGS = 1
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    FacebookSdk.sdkInitialize(this)
    AppEventsLogger.activateApp(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    setLocationPermission()
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

  private fun setLocationPermission() {
    Dexter.withActivity(this)
        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(object : PermissionListener {
          override fun onPermissionGranted(response: PermissionGrantedResponse?) {
            obtainLocation()
            initFacebookButton()
          }

          override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
            token?.continuePermissionRequest()
          }

          override fun onPermissionDenied(response: PermissionDeniedResponse?) {
            showSnack(R.string.userLogin_permission_denied_advice_messa,
                R.string.userlogin_accept_permission_button, { goToPermissions() })
          }
        }).check()
  }

  private fun goToPermissions() {
    Intent().apply {
      action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
      data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
      startActivityForResult(this, REQUEST_CODE_SETTINGS)
    }
  }

  @SuppressLint("MissingPermission")
  private fun obtainLocation() {
    val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    if (location != null) {
      initFacebookButton()
      saveLocationInPreferences(location.latitude, location.longitude)
    } else {
      showsLocationsDialog()
    }
  }

  private fun saveLocationInPreferences(latitude: Double, longitude: Double) {
    userRepository.apply {
      saveLatitude(latitude)
      saveLongitude(longitude)
    }
  }

  private fun showsLocationsDialog() {
    alert(R.string.userLogin_allow_location_message) {
      positiveButton(R.string.accept, { openSettings() })
      negativeButton(R.string.cancel, {
        showSnack(R.string.userlogin_open_settings_message,
            R.string.userlogin_open_settings_button,
            { openSettings() })
      })
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

  private fun goToSelectTypeUserActivity() {
    finish()
    startActivity<SelectTypeUserActivity>()
  }

  private fun goToSearchActivity() {
    finish()
    startActivity<SearchActivity>()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_SETTINGS) {
      obtainLocation()
    }
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  private fun initFacebookButton() {
    login_facebook_button.visibility = VISIBLE
    login_facebook_button.setReadPermissions("email", "public_profile", "user_birthday", "user_location", "location")
    val facebookCallback = object : FacebookCallback<LoginResult> {
      override fun onSuccess(loginResult: LoginResult) {
        login_progress_bar.visibility = VISIBLE
        loginFacebookUser()
      }

      override fun onCancel() {
      }

      override fun onError(error: FacebookException) {
      }
    }
    login_facebook_button.registerCallback(callbackManager, facebookCallback)
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
        with(userRepository) {
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
