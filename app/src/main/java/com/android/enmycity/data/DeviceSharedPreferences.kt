package com.android.enmycity.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class DeviceSharedPreferences(context: Context) {
  companion object {
    private const val TOKEN = "DEVICE_PREFERENCES_TOKEN"
  }

  private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val editor: SharedPreferences.Editor

  init {
    editor = sharedPreferences.edit()
  }

  fun getToken() = sharedPreferences.getString(TOKEN, "")

  fun saveToken(token: String) = editor.putString(TOKEN, token).apply()
}