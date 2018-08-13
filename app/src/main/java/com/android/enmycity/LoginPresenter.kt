package com.android.enmycity

import com.android.enmycity.data.AccountPreferencesDao
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.AccountCreationPreferences
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class LoginPresenter(
    private val firebaseAuth: FirebaseAuth,
    private val accountCreationPreferences: AccountCreationPreferences,
    private val userSharedPreferences: UserSharedPreferences
) {

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

  fun loginGoogleUser(googleSignInAccount: GoogleSignInAccount) = signInUser(
      GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null))

  fun loginFacebookUser(accessToken: AccessToken) = signInUser(FacebookAuthProvider.getCredential(accessToken.token))

  private fun signInUser(credential: AuthCredential) {
    firebaseAuth
        .signInWithCredential(credential)
        .addOnCompleteListener {
          if (it.isSuccessful) {
            saveFirebaseUser(it.result.user)
          }
        }
        .addOnFailureListener { view.showLoginError(it.message ?: "") }
  }

  private fun saveFirebaseUser(firebaseUser: FirebaseUser) {
    with(accountCreationPreferences) {
      saveUserId(firebaseUser.uid)
      saveUserName(firebaseUser?.displayName ?: "")
      saveUserEmail(firebaseUser?.email ?: "")
      saveUserAvatar(firebaseUser?.photoUrl.toString())
    }
    checkIfUserExists()
  }

  private fun checkIfUserExists() {
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(firebaseAuth.uid ?: "")
        .get()
        .addOnSuccessListener {
          when (it.exists()) {
            true -> obtainUserData(it)
            false -> view.goToSelectUserTypeActivity()
          }
        }
  }

  private fun obtainUserData(documentSnapshot: DocumentSnapshot) {
    val user = documentSnapshot.toObject(AccountPreferencesDao::class.java) ?: AccountPreferencesDao()

    when (user.type) {
      3 -> view.goToLoadUserTypeActivity()
      2 -> getLocalAccount()
      1 -> getTravellerAccount()
    }
  }

  private fun getLocalAccount() = getAccount("locals")

  private fun getTravellerAccount() = getAccount("travellers")

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