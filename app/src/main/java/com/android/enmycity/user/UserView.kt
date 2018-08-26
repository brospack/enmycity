package com.android.enmycity.user

import com.android.enmycity.data.UserLogged

interface UserView {
  fun showUserData(userLogged: UserLogged)

  fun showSuccessfulUpdateMessage()

  fun showErrorUpdatingData(message: String)
}