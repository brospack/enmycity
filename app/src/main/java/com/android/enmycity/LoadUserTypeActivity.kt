package com.android.enmycity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_load_user_type.loadUserType_local
import kotlinx.android.synthetic.main.activity_load_user_type.loadUserType_progressBar
import kotlinx.android.synthetic.main.activity_load_user_type.loadUserType_traveller

class LoadUserTypeActivity : AppCompatActivity() {
  private val userSharedPreferences by lazy { UserSharedPreferences(this) }
  private var localSaved = false
  private var travellerSaved = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_load_user_type)
    saveProfilesInPreferences()
    loadUserType_local.setOnClickListener {
      userSharedPreferences.setLocalAsCurrent()
      openSearchActivity()
    }
    loadUserType_traveller.setOnClickListener {
      userSharedPreferences.setTravellerAsCurrent()
      openSearchActivity()
    }
  }

  private fun saveProfilesInPreferences() {
    loadLocalType()
    loadTravellerType()
  }

  private fun loadLocalType() {
    loadUserType("locals")
  }

  private fun loadTravellerType() {
    loadUserType("travellers")
  }

  private fun loadUserType(type: String) {
    val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
    FirebaseFirestore.getInstance()
        .collection(type)
        .document(email)
        .get()
        .addOnSuccessListener {
          if (it.exists()) {
            loadUserInPreferences(it.toObject(UserDao::class.java), type)
          }
        }
  }

  private fun loadUserInPreferences(userDao: UserDao, typeUser: String) {
    when (typeUser) {
      "locals" -> saveLocalInPreferences(userDao)
      "travellers" -> saveTravellerInPreferences(userDao)
    }
    if (localSaved && travellerSaved) {
      loadUserType_progressBar.visibility = GONE
    }
  }

  private fun saveLocalInPreferences(userDao: UserDao) {
    userSharedPreferences.saveUserLocal(userDao)
    localSaved = true
  }

  private fun saveTravellerInPreferences(userDao: UserDao) {
    userSharedPreferences.saveUserTraveller(userDao)
    travellerSaved = true
  }
}