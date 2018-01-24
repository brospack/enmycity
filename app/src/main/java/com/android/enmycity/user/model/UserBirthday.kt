package com.android.enmycity.user.model

data class UserBirthday(val day: Int = 0, val month: Int = 0, val year: Int = 0) {

  private fun getAge() =
      when (day > 0 && month > 0 && year > 0) {
        true -> 1
        false -> 0
      }

}