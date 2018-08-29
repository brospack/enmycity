package com.android.enmycity.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserSharedPreferences(
    context: Context,
    private val gson: Gson = Gson()
) {
  companion object {
    private const val USER_LOGGED = "USER_LOGGED_PREFERENCES"
  }

  private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val editor: SharedPreferences.Editor

  init {
    editor = sharedPreferences.edit()
  }

  fun saveUserLogged(user: User) {
    with(editor) {
      putString(USER_LOGGED, gson.toJson(user))
      apply()
    }
  }

  fun getUserLogged(): User {
    var userLogged = User()
    val userLoggedGsoned = sharedPreferences.getString(USER_LOGGED, null)

    gson.let {
      val type = object : TypeToken<User>() {}.type
      userLogged = it.fromJson(userLoggedGsoned, type)
    }
    return userLogged
  }

  fun isUserLogged() = getUserLogged().id.isNotEmpty()

  fun clear() = editor.clear()
}