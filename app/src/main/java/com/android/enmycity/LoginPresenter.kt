package com.android.enmycity

import com.android.enmycity.accountCreation.selectTypeUser.UserAccountDao
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.MapUserDaoToUser
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.AccountCreationPreferences
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
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
            saveFirebaseUserInPreferences(it.result.user)
          }
        }
        .addOnFailureListener { view.showLoginError(it.message.orEmpty()) }
  }

  private fun saveFirebaseUserInPreferences(firebaseUser: FirebaseUser) {
    with(accountCreationPreferences) {
      saveUserId(firebaseUser.uid)
      saveUserName(firebaseUser.displayName.orEmpty())
      saveUserEmail(firebaseUser.email.orEmpty())
      saveUserAvatar(firebaseUser.photoUrl.toString())
    }
    checkIfUserExists()
  }

  private fun checkIfUserExists() {
    FirebaseFirestore.getInstance()
        .collection(FirestoreCollectionNames.USERS)
        .document(firebaseAuth.uid.orEmpty())
        .get()
        .addOnSuccessListener {
          val userAccountDao = it.toObject(UserAccountDao::class.java) ?: UserAccountDao()
          if (it.exists()) obtainUserData(userAccountDao) else view.goToSelectUserTypeActivity()
        }
  }

  private fun obtainUserData(userAccountDao: UserAccountDao) {
    when (userAccountDao.type) {
      3 -> view.goToLoadUserTypeActivity()
      2 -> getLocalAccount(userAccountDao.localId)
      1 -> getTravellerAccount(userAccountDao.travellerId)
    }
  }

  private fun getLocalAccount(localId: String) = getAccount(FirestoreCollectionNames.LOCALS, localId)

  private fun getTravellerAccount(travellerId: String) = getAccount(FirestoreCollectionNames.TRAVELLERS, travellerId)

  private fun getAccount(collectionName: String, documentId: String) {
    FirebaseFirestore.getInstance()
        .collection(collectionName)
        .document(documentId)
        .get()
        .addOnSuccessListener {
          if (it.exists()) {
            val userDao = it.toObject(UserDao::class.java) ?: UserDao()
            val userLogged = MapUserDaoToUser().map(userDao, collectionName, it.id)
            userSharedPreferences.saveUserLogged(userLogged)
            view.goToMainActivity()
          } else view.goToSelectUserTypeActivity()
        }
  }
}