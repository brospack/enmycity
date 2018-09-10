package com.android.enmycity.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.doubleToLongBits
import com.android.enmycity.longBitsToDouble

class AccountCreationPreferences(context: Context) {
  companion object {
    private const val USER_ID = "ACCOUNT_CREATION_PREFERENCES_USER_ID"
    private const val USER_PLACE_ID = "ACCOUNT_CREATION_PREFERENCES_PLACE_ID"
    private const val USER_NAME = "ACCOUNT_CREATION_PREFERENCES_NAME"
    private const val USER_EMAIL = "ACCOUNT_CREATION_PREFERENCES_EMAIL"
    private const val USER_AVATAR = "ACCOUNT_CREATION_PREFERENCES_AVATAR"
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

  fun getUserId(): String = sharedPreferences.getString(USER_ID, "")

  fun saveUserName(name: String) = saveStringValue(USER_NAME, name)

  fun getUserName(): String = sharedPreferences.getString(USER_NAME, "")

  fun saveUserEmail(email: String) = saveStringValue(USER_EMAIL, email)

  fun getUserEmail(): String = sharedPreferences.getString(USER_EMAIL, "")

  fun saveUserAvatar(avatarUrl: String) = saveStringValue(USER_AVATAR, avatarUrl)

  fun getUserAvatar(): String = sharedPreferences.getString(USER_AVATAR, "")

  fun saveLatitude(latitude: Double) = saveDoubleValue(USER_LATITUDE, latitude)

  fun getLatitude() = sharedPreferences.getLong(USER_LATITUDE, 0L).longBitsToDouble()

  fun saveLongitude(longitude: Double) = saveDoubleValue(USER_LONGITUDE, longitude)

  fun getLongitude() = sharedPreferences.getLong(USER_LONGITUDE, 0L).longBitsToDouble()

  fun savePlaceId(placeId: String) = saveStringValue(USER_PLACE_ID, placeId)

  fun getPlaceId() = sharedPreferences.getString(USER_PLACE_ID, "")

  fun setIsUserLocal(isLocal: Boolean) = with(editor) {
    putBoolean(USER_IS_LOCAL, isLocal)
    apply()
  }

  fun getUserType() = sharedPreferences.getBoolean(USER_IS_LOCAL, false)
      .let {
        when (it) {
          true -> FirestoreCollectionNames.LOCALS
          false -> FirestoreCollectionNames.TRAVELLERS
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