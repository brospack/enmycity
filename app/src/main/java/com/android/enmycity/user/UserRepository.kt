package com.android.enmycity.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.enmycity.doubleToLongBits
import com.android.enmycity.longBitsToDouble

class UserRepository(context: Context) {
  companion object {
    private val USER_ID = "PREFERENCES_USER_ID"
    private val USER_NAME = "PREFERENCES_USER_NAME"
    private val USER_EMAIL = "PREFERENCES_USER_EMAIL"
    private val USER_AVATAR = "PREFERENCES_USER_AVATAR"
    private val USER_BIRTHDAY = "PREFERENCES_USER_BIRTHDAY"
    private val USER_CITY = "PREFERENCES_USER_CITY"
    private val USER_GENDER = "PREFERENCES_USER_GENDER"
    private val USER_IS_LOCAL = "PREFERENCES_USER_IS_LOCAL"
    private val USER_IS_CREATED = "PREFERENCES_USER_IS_CREATED"
    private val USER_LATITUDE = "PREFERENCES_USER_LATITUDE"
    private val USER_LONGITUDE = "PREFERENCES_USER_LONGITUDE"
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

  fun setIsUserCreated(isFinished: Boolean) = with(editor) {
    putBoolean(USER_IS_CREATED, isFinished)
    apply()
  }

  fun isUserCreated() = sharedPreferences.getBoolean(USER_IS_CREATED, false)

  fun getUserGender() =
      sharedPreferences.getString(USER_GENDER, "").let {
        when (it.toLowerCase() == "male") {
          true -> 0
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

  private fun saveStringValue(preferenceName: String, value: String) = with(editor) {
    putString(preferenceName, value)
    apply()
  }

  private fun saveDoubleValue(preferenceName: String, value: Double) = with(editor) {
    putLong(preferenceName, value.doubleToLongBits())
    apply()
  }
}