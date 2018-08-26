package com.android.enmycity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.data.MapUserLoggedFromUserDao
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserLogged
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.model.GenderType
import com.android.enmycity.user.model.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_load_user_type.loadUserType_local
import kotlinx.android.synthetic.main.activity_load_user_type.loadUserType_traveller

class LoadUserTypeActivity : AppCompatActivity() {
  private val userSharedPreferences by lazy { UserSharedPreferences(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_load_user_type)
    loadUserType_local.setOnClickListener {
      loadLocalUser()
    }
    loadUserType_traveller.setOnClickListener {
      loadTravellerUser()
    }
  }

  private fun loadLocalUser() {
    getUserData(FirestoreCollectionNames.LOCALS)
  }

  private fun loadTravellerUser() {
    getUserData(FirestoreCollectionNames.TRAVELLERS)
  }

  private fun getUserData(collection: String) {
    val uid = FirebaseAuth.getInstance().uid

    FirebaseFirestore.getInstance()
        .collection(collection)
        .whereEqualTo("uid", uid)
        .get()
        .addOnSuccessListener {
          if (it.documents.isNotEmpty()) {
            val document = it.documents.first()
            val userDao: UserDao = document.toObject(UserDao::class.java) ?: UserDao()
            val userLogged = MapUserLoggedFromUserDao().map(userDao, collection, document.id)
            userSharedPreferences.saveUserLogged(userLogged)
            openSearchActivity()
          }
        }
  }
}