package com.android.enmycity.main

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class MyOtherPreferences(context: Context) {
  companion object {
    private val NAME = "PREFERENCES-NAME"
    private val AGE = "PREFERENCES-AGE"
  }

  private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val editor: SharedPreferences.Editor

  init {
    editor = sharedPreferences.edit()
  }

  fun setName(name: String) {
    with(editor) {
      putString(NAME, name)
      apply()
    }
  }

  fun setAge(age: Int) {
    with(editor) {
      putInt(AGE, age)
      apply()
    }
  }

  fun getName() = sharedPreferences.getString(NAME, "")

  fun getAge() = sharedPreferences.getInt(AGE, 0)

  fun clear() {
    with(editor) {
      clear()
      apply()
    }
  }

}