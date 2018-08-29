package com.android.enmycity.chats

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ChatsPresenter(
    private val firebaseFirestore: FirebaseFirestore,
    private val userSharedPreferences: UserSharedPreferences
) {
  private lateinit var view: ChatsView

  fun setView(view: ChatsView) {
    this.view = view
  }

  fun onViewReady() {
    getChatsAsOwner()
    getChatsAsGuest()
  }

  private fun getChatsAsOwner() {
    firebaseFirestore.collection(FirestoreCollectionNames.CHATS)
        .whereEqualTo("ownerId", userSharedPreferences.getUserLogged().id)
        .whereEqualTo("status", 2)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.documents
                .map {
                  val chatDto = it.toObject(ChatDto::class.java) ?: ChatDto()
                  Chat(it.id, chatDto.guestName, chatDto.guestPhoto)
                }
                .forEach {
                  view.showChat(it)
                }
          }
        }
  }

  private fun getChatsAsGuest() {
    firebaseFirestore.collection(FirestoreCollectionNames.CHATS)
        .whereEqualTo("guestId", userSharedPreferences.getUserLogged().id)
        .whereEqualTo("status", 2)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.documents
                .map {
                  val chatDto = it.toObject(ChatDto::class.java) ?: ChatDto()
                  Chat(it.id, chatDto.ownerName, chatDto.ownerPhoto)
                }
                .forEach {
                  view.showChat(it)
                }
          }
        }
        .addOnFailureListener { }
        .addOnCompleteListener { view.hiddeProgressBar() }
  }
}