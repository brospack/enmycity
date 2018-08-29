package com.android.enmycity.user

import com.android.enmycity.data.User

interface UserView {
  fun showUserData(user: User)

  fun showSuccessfulUpdateMessage()

  fun showErrorUpdatingData(message: String)
}