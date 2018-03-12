package com.android.enmycity.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserSharedPreferences(context: Context,
                            private val gson: Gson = Gson()
) {
  companion object {
    private const val TRAVELLER_USER = "USER_ACCOUNT_PREFERENCES_TRAVELLER_ACCOUNT"
    private const val LOCAL_USER = "USER_ACCOUNT_PREFERENCES_LOCAL_ACCOUNT"
    private const val IS_USER_LOADED = "USER_ACCOUNT_PREFERENCES_IS_USER_LOADED"
    private const val CURRENT_USER_TYPE = "USER_ACCOUNT_PREFERENCES_CURRENT_USER_TYPE"
    const val USER_TYPE_TRAVELLER = "TRAVELLER"
    const val USER_TYPE_LOCAL = "LOCAL"
  }

  private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val editor: SharedPreferences.Editor

  init {
    editor = sharedPreferences.edit()
  }

  fun saveUserTraveller(userDao: UserDao) = saveUserDao(userDao, TRAVELLER_USER)

  fun getUserTraveller() = getFromGson(TRAVELLER_USER)

  fun saveUserLocal(userDao: UserDao) = saveUserDao(userDao, LOCAL_USER)

  fun getUserLocal() = getFromGson(LOCAL_USER)

  fun setTravellerAsCurrent() = setCurrentUserType(USER_TYPE_TRAVELLER)

  fun setLocalAsCurrent() = setCurrentUserType(USER_TYPE_LOCAL)

  fun getCurrentUser() = getCurrentUserType().let {
    if (it == USER_TYPE_LOCAL) {
      getUserLocal()
    } else {
      getUserTraveller()
    }
  }

  fun getCurrentUserType(): String = sharedPreferences.getString(CURRENT_USER_TYPE, "")

  private fun setCurrentUserType(userType: String) = with(editor) {
    putString(CURRENT_USER_TYPE, userType)
    apply()
  }

  fun isUserLoaded() = sharedPreferences.getBoolean(IS_USER_LOADED, false)

  private fun saveUserDao(userDao: UserDao, userType: String) = with(editor) {
    putString(userType, gson.toJson(userDao))
    putBoolean(IS_USER_LOADED, true)
    apply()

    when (userType) {
      LOCAL_USER -> setLocalAsCurrent()
      TRAVELLER_USER -> setTravellerAsCurrent()
    }
  }

  private fun getFromGson(userType: String): UserDao {
    var userDao = UserDao()
    val gsonUser = sharedPreferences.getString(userType, null)
    gson.let {
      val type = object : TypeToken<UserDao>() {
      }.type
      userDao = gson.fromJson(gsonUser, type)
    }
    return userDao
  }

  fun clear() = editor.clear()
}