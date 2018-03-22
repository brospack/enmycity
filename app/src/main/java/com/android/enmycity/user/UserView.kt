package com.android.enmycity.user

import com.android.enmycity.data.UserDao

interface UserView {
  fun showUserData(userDao: UserDao)
}