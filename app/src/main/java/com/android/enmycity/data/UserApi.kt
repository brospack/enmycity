package com.android.enmycity.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserApi(private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

  fun getUserTraveller(email: String): UserDao {
    var userDao = UserDao()
    val docRefer = firebaseFirestore.collection("locals").document(email)
    docRefer.get().addOnSuccessListener {
      if (it.exists()) {
        userDao = it.toObject(UserDao::class.java)!!
      }
    }
    return userDao
  }
}