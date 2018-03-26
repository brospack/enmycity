package com.android.enmycity

import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.user.AccountCreationPreferences
import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(private val firabaseAuthentication: FirebaseAuth,
                     private val accountCreationPreferences: AccountCreationPreferences,
                     private val userSharedPreferences: UserSharedPreferences) {
}