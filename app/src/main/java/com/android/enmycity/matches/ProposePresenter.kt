package com.android.enmycity.matches

import com.android.enmycity.chats.ChatDto
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore

class ProposePresenter(private val userSharedPreferences: UserSharedPreferences, private val firestore: FirebaseFirestore) {
  private lateinit var view: ProposeView

  fun setView(proposeView: ProposeView) {
    view = proposeView
  }

  fun onViewReady() {
    userSharedPreferences.getUserLogged().let {
      getPendingProposals(it.id)
      getUserProposals(it.id)
    }
  }

  private fun getUserProposals(userId: String) {
    firestore.collection(FirestoreCollectionNames.CHATS)
        .whereEqualTo("guestId", userId)
        .whereEqualTo("status", 1)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.documents
                .map {
                  val chatDto = it.toObject(ChatDto::class.java) ?: ChatDto()
                  ProposeViewModel(it.id, chatDto.guestId, chatDto.guestName, chatDto.guestPhoto, isOwner = false)
                }
                .forEach {
                  view.showPropose(it)
                }
          }
        }
  }

  private fun getPendingProposals(userId: String) {
    firestore.collection(FirestoreCollectionNames.CHATS)
        .whereEqualTo("ownerId", userId)
        .whereEqualTo("status", 1)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.documents
                .map {
                  val chatDto = it.toObject(ChatDto::class.java) ?: ChatDto()
                  ProposeViewModel(it.id, chatDto.guestId, chatDto.guestName, chatDto.guestPhoto, isOwner = true)
                }
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