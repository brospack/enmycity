package com.android.enmycity

import com.android.enmycity.data.AccountPreferencesDao
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.AccountCreationPreferences
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject

class LoginPresenter(private val firabaseAuthentication: FirebaseAuth,
                     private val accountCreationPreferences: AccountCreationPreferences,
                     private val userSharedPreferences: UserSharedPreferences) {

  private lateinit var view: LoginView

  fun setView(view: LoginView) {
    this.view = view
  }

  fun onViewReady() {
    view.getLocation()
  }

  fun saveLocationInPreferences(latitude: Double, longitude: Double) {
    accountCreationPreferences.apply {
      saveLatitude(latitude)
      saveLongitude(longitude)
    }
  }

  fun loginFacebookUser(accessToken: AccessToken) {
    val facebookCredential = FacebookAuthProvider.getCredential(accessToken.token)
    firabaseAuthentication.signInWithCredential(facebookCredential)
        .addOnCompleteListener {
          if (it.isSuccessful) {
            view.createUser(accessToken)
          }
        }
  }

  fun saveUserDataInPreferences(jsonObject: JSONObject) {
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

  private fun checkIfUserExists() {
    FirebaseFirestore.getInstance()
        .collection("accountPreferences")
        .document(accountCreationPreferences.getUserEmail())
        .get()
        .addOnSuccessListener {
          when (it.exists()) {
            true -> obtainUserData(it)
            false -> view.goToSelectUserTypeActivity()
          }
        }
  }

  private fun obtainUserData(documentSnapshot: DocumentSnapshot) {
    val accountPreferences = documentSnapshot.toObject(AccountPreferencesDao::class.java)
    if (accountPreferences!!.isLocal && accountPreferences.isTraveller) {
      view.goToLoadUserTypeActivity()
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
            loadUserInPreferences(it.toObject(UserDao::class.java)!!, typeUser)
            view.goToMainActivity()
          } else view.goToSelectUserTypeActivity()
        }
  }

  private fun loadUserInPreferences(userDao: UserDao, typeUser: String) {
    when (typeUser) {
      "locals" -> userSharedPreferences.saveUserLocal(userDao)
      "travellers" -> userSharedPreferences.saveUserTraveller(userDao)
    }
  }
}