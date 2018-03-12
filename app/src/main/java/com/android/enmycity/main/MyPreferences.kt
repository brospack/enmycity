package com.android.enmycity.main

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class MyPreferences(context: Context) {
  companion object {
    private val SHARED_PREFERENCES = "MY_SHARED_PREFERENCES"
    private val NAME = "$SHARED_PREFERENCES-NAME"
    private val AGE = "$SHARED_PREFERENCES-AGE"
  }

  private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
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