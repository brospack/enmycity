package com.android.enmycity.profile

import com.android.enmycity.data.UserDao

interface ProfileView {
  fun showData(userDao: UserDao)
}