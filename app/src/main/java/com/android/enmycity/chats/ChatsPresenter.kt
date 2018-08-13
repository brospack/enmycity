package com.android.enmycity.chats

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ChatsPresenter(
    private val firebaseFirestore: FirebaseFirestore, private val firebaseDatabase: FirebaseDatabase,
    private val userSharedPreferences: UserSharedPreferences
) {
  private lateinit var view: ChatsView

  fun setView(view: ChatsView) {
    this.view = view
  }

  fun onViewReady() {
    val user = userSharedPreferences.getCurrentUser()
    val userType = userSharedPreferences.getCurrentUserType()

    firebaseFirestore.collection(FirestoreCollectionNames.USER_CHATS)
        .whereEqualTo("uid", userSharedPreferences.getCurrentUser().uid)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.forEach {
            }
          }
        }
  }

  private fun getLocalChats() {
  }

  private fun getTravellerChats() {
  }
}