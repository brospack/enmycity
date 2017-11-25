package com.android.enmycity.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class UserRepository(context: Context) {
  companion object {
    private val PREFERENCES_USER = "PREFERENCES_USER"
  }

  private var sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private var editor: SharedPreferences.Editor
  private val gson = Gson()

  init {
    editor = sharedPreferences.edit()
  }

  fun saveUser(user: FirebaseUser) {

  }

  private fun getJsonUser(user: FirebaseUser) = gson.toJson(user)
}