package com.android.enmycity.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.enmycity.search.Search
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserSharedPreferences(
    context: Context,
    private val gson: Gson = Gson()
) {
  companion object {
    private const val USER_LOGGED = "USER_LOGGED_PREFERENCES"
    private const val LAST_SEARCH = "USER_PREFERENCES_LAST_SEARCH"
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

    if (userLoggedGsoned != null) {
      val type = object : TypeToken<User>() {}.type
      userLogged = gson.fromJson(userLoggedGsoned, type)
    }
    return userLogged
  }

  fun isUserLogged() = getUserLogged().id.isNotEmpty()

  fun saveLastSearch(search: Search) {
    with(editor) {
      putString(LAST_SEARCH, gson.toJson(search))
      apply()
    }
  }

  fun getLastSearch(): Search {
    var search = Search()
    val searchGsoned = sharedPreferences.getString(LAST_SEARCH, null)

    if (searchGsoned != null) {
      val type = object : TypeToken<Search>() {}.type
      search = gson.fromJson(searchGsoned, type)
    }

    return search
  }

  fun clear() = editor.clear()
}