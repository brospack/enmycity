package com.android.enmycity.profile

import com.android.enmycity.chats.ChatDto
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.common.StatusId
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore

class ProfilePresenter(

    private val userSharedPreferences: UserSharedPreferences
) {
  private lateinit var view: ProfileView
  private val firestore = FirebaseFirestore.getInstance()

  fun setView(profileView: ProfileView) {
    view = profileView
  }

  fun onProposeCreated(profileViewModel: ProfileViewModel, message: String = "") {
    val userLogged = userSharedPreferences.getUserLogged()

    val chatDto = ChatDto(
        status = StatusId.PENDING,
        userType = userLogged.userType.id,
        ownerId = userLogged.id,
        ownerName = userLogged.name,
        ownerPhoto = userLogged.photoUrl,
        guestId = profileViewModel.id,
        guestName = profileViewModel.name,
        guestPhoto = profileViewModel.photoUrl,
        userUid = userLogged.uid
    )

    firestore.collection(FirestoreCollectionNames.CHATS)
        .add(chatDto)
        .addOnCompleteListener { if (it.isSuccessful) view.proposeSendedMessage() else view.proposeDidntSended() }
        .addOnFailureListener { view.proposeDidntSended() }
  }
}