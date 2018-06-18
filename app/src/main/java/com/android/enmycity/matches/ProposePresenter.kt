package com.android.enmycity.matches

import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.common.StatusId
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProposePresenter(private val userSharedPreferences: UserSharedPreferences, private val firestore: FirebaseFirestore) {
  private lateinit var view: ProposeView

  fun setView(proposeView: ProposeView) {
    view = proposeView
  }

  fun onViewReady() {
    val userUid = FirebaseAuth.getInstance().uid

    firestore.collection(FirestoreCollectionNames.PROPOSALS)
        .whereEqualTo("proponentUid", userUid)
        .get()
        .addOnSuccessListener {
          if (!it.isEmpty) {
            it.documents.map { it.toObject(ProposeDto::class.java) ?: ProposeDto() }
                .filter { it.status == StatusId.PENDING }
                .map { ProposeViewModel(it.proposerUid, it.status, it.proposerName, it.proposerPhoto, isProponent = true) }
                .forEach {
                  view.hideLoading()
                  view.showPropose(it)
                }
          }
        }

    firestore.collection(FirestoreCollectionNames.PROPOSALS)
        .whereEqualTo("proposerUid", userUid)
        .get()
        .addOnSuccessListener {
          it.documents.map { it.toObject(ProposeDto::class.java) ?: ProposeDto() }
              .filter { it.status == StatusId.PENDING }
              .map { ProposeViewModel(it.proposerUid, it.status, it.proposerName, it.proposerPhoto, isProponent = false) }
              .forEach {
                view.hideLoading()
                view.showPropose(it)
              }
        }
  }
}