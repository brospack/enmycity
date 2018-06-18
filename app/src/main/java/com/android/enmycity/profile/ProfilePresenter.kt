package com.android.enmycity.profile

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.matches.ProposeDto
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePresenter(
    private val firebaseFirestore: FirebaseFirestore,
    private val userSharedPreferences: UserSharedPreferences
) {
  private lateinit var view: ProfileView

  fun setView(profileView: ProfileView) {
    view = profileView
  }

  fun onProposeCreated(profileViewModel: ProfileViewModel, message: String = "") {
    val mainUser = userSharedPreferences.getCurrentUser()
    val propose = ProposeDto(
        proponentUid = mainUser.uid,
        proponentName = mainUser.name,
        proponentPhoto = mainUser.photoUrl,
        proposerUid = profileViewModel.uid,
        proposerName = profileViewModel.name,
        proposerPhoto = profileViewModel.photoUrl,
        message = message
    )
    firebaseFirestore.collection(FirestoreCollectionNames.PROPOSALS).add(propose)
        .addOnCompleteListener { if (it.isSuccessful) view.proposeSendedMessage() else view.proposeDidntSended() }
        .addOnFailureListener { view.proposeDidntSended() }
  }
}