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
import android.view.View.VISIBLE
import com.android.enmycity.data.AccountPreferencesDao
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.AccountCreationPreferences
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.login_facebook_button
import kotlinx.android.synthetic.main.activity_login.login_progress_bar
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class UserLoginActivity : AppCompatActivity() {
  private val callbackManager = CallbackManager.Factory.create()
  private val firabaseAuthentication: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
  private val accountCreationPreferences: AccountCreationPreferences by lazy { AccountCreationPreferences(this) }
  private val userSharedPreferences: UserSharedPreferences by lazy { UserSharedPreferences(this) }
  private val locationManager: LocationManager by lazy { this.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

  companion object {
    private const val REQUEST_CODE_SETTINGS = 1
    private val PERMISSIONS = listOf("email", "public_profile", "user_birthday", "user_location")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    FacebookSdk.sdkInitialize(this)
    AppEventsLogger.activateApp(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    obtainLocation()
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
    accountCreationPreferences.apply {
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

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_SETTINGS) {
      obtainLocation()
    }
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  private fun initFacebookButton() {
    login_facebook_button.visibility = VISIBLE
    login_facebook_button.setReadPermissions(PERMISSIONS)
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
        with(accountCreationPreferences) {
          saveUserId(jsonObject.getString("id"))
          saveUserName(jsonObject.getString("name"))
          saveUserEmail(jsonObject.getString("email"))
          saveUserGender(jsonObject.getString("gender"))
          saveUserBirthday(jsonObject.getString("birthday"))
          saveUserCity(jsonObject.getJSONObject("location").getString("name"))
          saveUserAvatar("http://graph.facebook.com/${jsonObject.getString("id")}/picture?type=large")
        }
        checkIfUserExists()
      }
    }
    GraphRequest.newMeRequest(accessToken, graphJsonCallback).apply {
      parameters = Bundle().apply { putString("fields", "id,name,email,gender,birthday,location") }
      executeAsync()
    }
  }

  private fun checkIfUserExists() {
    FirebaseFirestore.getInstance()
        .collection("accountPreferences")
        .document(accountCreationPreferences.getUserEmail())
        .get()
        .addOnSuccessListener {
          when (it.exists()) {
            true -> obtainUserData(it)
            false -> openSelectUserTypeActivity()
          }
        }
  }

  private fun obtainUserData(documentSnapshot: DocumentSnapshot) {
    val accountPreferences = documentSnapshot.toObject(AccountPreferencesDao::class.java)
    if (accountPreferences.isLocal && accountPreferences.isTraveller) {
      openLoadUserTypeActivity()
    } else if (accountPreferences.isTraveller) {
      getTravellerAccount()
    } else {
      getLocalAccount()
    }
  }

  private fun getLocalAccount() {
    getAccount("locals")
  }

  private fun getTravellerAccount() {
    getAccount("travellers")
  }

  private fun getAccount(typeUser: String) {
    FirebaseFirestore.getInstance()
        .collection(typeUser)
        .document(accountCreationPreferences.getUserEmail())
        .get()
        .addOnSuccessListener {
          if (it.exists()) {
            loadUserInPreferences(it.toObject(UserDao::class.java), typeUser)
            openSearchActivity()
          } else openSelectUserTypeActivity()
        }
  }

  private fun loadUserInPreferences(userDao: UserDao, typeUser: String) {
    when (typeUser) {
      "locals" -> userSharedPreferences.saveUserLocal(userDao)
      "travellers" -> userSharedPreferences.saveUserTraveller(userDao)
    }
  }
}
