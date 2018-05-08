package com.android.enmycity.profile

import com.android.enmycity.data.UserSharedPreferences
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.functions.FirebaseFunctions
import java.util.HashMap

class ProfilePresenter(private val firebaseDatabase: FirebaseDatabase,
                       private val userSharedPreferences: UserSharedPreferences) {
  private lateinit var view: ProfileView

  fun setView(profileView: ProfileView) {
    view = profileView
  }

  fun onProposeCreated(email:String){
    val db = firebaseDatabase.reference
    addMessage("hola")
        .addOnCompleteListener {  }

  }

  private fun addMessage(text:String): Task<String>{
    val functions = FirebaseFunctions.getInstance()
    val data = HashMap<String,String>()
    data.put("ola","k ase")

    return functions.getHttpsCallable("addMessage")
        .call(data)
        .continueWith { it.result.data.toString()}
  }
}