package com.android.enmycity.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.enmycity.doubleToLongBits
import com.android.enmycity.longBitsToDouble

class AccountCreationPreferences(context: Context) {
  companion object {
    private const val USER_ID = "ACCOUNT_CREATION_PREFERENCES_USER_ID"
    private const val USER_NAME = "ACCOUNT_CREATION_PREFERENCES_NAME"
    private const val USER_EMAIL = "ACCOUNT_CREATION_PREFERENCES_EMAIL"
    private const val USER_AVATAR = "ACCOUNT_CREATION_PREFERENCES_AVATAR"
    private const val USER_BIRTHDAY = "ACCOUNT_CREATION_PREFERENCES_BIRTHDAY"
    private const val USER_CITY = "ACCOUNT_CREATION_PREFERENCES_USER_CITY"
    private const val USER_GENDER = "ACCOUNT_CREATION_PREFERENCES_GENDER"
    private const val USER_IS_LOCAL = "ACCOUNT_CREATION_PREFERENCES_IS_LOCAL"
    private const val USER_LATITUDE = "ACCOUNT_CREATION_PREFERENCES_LATITUDE"
    private const val USER_LONGITUDE = "ACCOUNT_CREATION_PREFERENCES_LONGITUDE"

  }

  private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val editor: SharedPreferences.Editor

  init {
    editor = sharedPreferences.edit()
  }

  fun saveUserId(id: String) = saveStringValue(USER_ID, id)

  fun saveUserName(name: String) = saveStringValue(USER_NAME, name)

  fun getUserName(): String = sharedPreferences.getString(USER_NAME, "")

  fun saveUserEmail(email: String) = saveStringValue(USER_EMAIL, email)

  fun getUserEmail(): String = sharedPreferences.getString(USER_EMAIL, "")

  fun saveUserAvatar(avatarUrl: String) = saveStringValue(USER_AVATAR, avatarUrl)

  fun getUserAvatar(): String = sharedPreferences.getString(USER_AVATAR, "")

  fun saveUserBirthday(birthday: String) = saveStringValue(USER_BIRTHDAY, birthday)

  fun getUserBirthday() = sharedPreferences.getString(USER_BIRTHDAY, "")

  fun saveUserCity(city: String) = saveStringValue(USER_CITY, city)

  fun getUserCity(): String = sharedPreferences.getString(USER_CITY, "")

  fun saveUserGender(gender: String) = saveStringValue(USER_GENDER, gender)

  fun saveLatitude(latitude: Double) = saveDoubleValue(USER_LATITUDE, latitude)

  fun getLatitude() = sharedPreferences.getLong(USER_LATITUDE, 0L).longBitsToDouble()

  fun saveLongitude(longitude: Double) = saveDoubleValue(USER_LONGITUDE, longitude)

  fun getLongitude() = sharedPreferences.getLong(USER_LONGITUDE, 0L).longBitsToDouble()

  fun getUserGender() =
      sharedPreferences.getString(USER_GENDER, "").let {
        when (it.toLowerCase() == "male") {
          true -> 2
          false -> 1
        }
      }

  fun setIsUserLocal(isLocal: Boolean) = with(editor) {
    putBoolean(USER_IS_LOCAL, isLocal)
    apply()
  }

  fun getUserType() = sharedPreferences.getBoolean(USER_IS_LOCAL, false).let {
    when (it) {
      true -> "locals"
      false -> "travellers"
    }
  }

  fun clear() {
    editor.clear()
  }

  private fun saveStringValue(preferenceName: String, value: String) = with(editor) {
    putString(preferenceName, value)
    apply()
  }

  private fun saveDoubleValue(preferenceName: String, value: Double) = with(editor) {
    putLong(preferenceName, value.doubleToLongBits())
    apply()
  }
}