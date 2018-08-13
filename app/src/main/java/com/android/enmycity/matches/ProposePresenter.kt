package com.android.enmycity.matches

import com.android.enmycity.chats.ChatDto
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProposePresenter(private val userSharedPreferences: UserSharedPreferences, private val firestore: FirebaseFirestore) {
  private lateinit var view: ProposeView

  fun setView(proposeView: ProposeView) {
    view = proposeView
  }

  fun onViewReady() {
    getPendingProposals()
    getUserProposals()
  }

  private fun getUserProposals() {
  }

  private fun getPendingProposals() {
    val userType = userSharedPreferences.getCurrentUserType()
    val userUid = FirebaseAuth.getInstance().uid
    firestore.collection(FirestoreCollectionNames.USER_CHATS)
        .whereEqualTo("ownerId", userUid)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.documents.map { it.toObject(ChatDto::class.java) ?: ChatDto() }
                .filter { it.status == 1 }
                .filter { it.ownerId == userUid }
                .map { ProposeViewModel(it.localId, it.status, it.guestName, it.guestPhoto, isProponent = true) }
                .forEach {
                  view.showPropose(it)
                }
          }
        }
        .addOnFailureListener { view.showEmptyData() }
        .addOnCompleteListener {
          view.hideLoading()
        }
  }
}