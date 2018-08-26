package com.android.enmycity.accountCreation.selectTypeUser

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.user.AccountCreationPreferences
import com.android.enmycity.user.model.UserType
import com.google.firebase.firestore.FirebaseFirestore

class SelectTypeUserPresenter(
    private val accountCreationPreferences: AccountCreationPreferences
) {
  private lateinit var view: SelectTypeUserView

  fun setView(view: SelectTypeUserView) {
    this.view = view
  }

  fun onViewReady() {
    accountCreationPreferences.let {
      view.showUserImage(it.getUserAvatar())
      view.showUserName(it.getUserName())
    }
  }

  fun onTravellerCreated() {
    accountCreationPreferences.setIsUserLocal(false)
    createUserAccount(UserType.TRAVELLER)
  }

  fun onLocalCreated() {
    accountCreationPreferences.setIsUserLocal(true)
    createUserAccount(UserType.LOCAL)
  }

  private fun createUserAccount(userType: UserType) {
    val userAccountDao = UserAccountDao(accountCreationPreferences.getUserId(),
        accountCreationPreferences.getUserEmail(),
        accountCreationPreferences.getUserName(),
        accountCreationPreferences.getUserAvatar(),
        userType.id)
    FirebaseFirestore.getInstance()
        .collection(FirestoreCollectionNames.USERS)
        .document(accountCreationPreferences.getUserId())
        .set(userAccountDao)
        .addOnSuccessListener {
          view.goToInterestsActivity()
        }
        .addOnFailureListener { view.showError() }
  }
}