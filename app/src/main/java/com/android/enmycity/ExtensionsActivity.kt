package com.android.enmycity

import android.app.Activity

import com.android.enmycity.accountCreation.InterestsActivity
import com.android.enmycity.main.InitActivity
import com.android.enmycity.main.UserMainActivity
import com.android.enmycity.accountCreation.selectTypeUser.SelectTypeUserActivity
import org.jetbrains.anko.startActivity

fun Activity.openMainActivity() {
  startActivity<InitActivity>()
  this.finish()
}

fun Activity.openLoginActivity() {
  startActivity<UserLoginActivity>()
  finish()
}

fun Activity.openSelectUserTypeActivity() {
  startActivity<SelectTypeUserActivity>()
  this.finish()
}

fun Activity.openInterestsActivity() {
  startActivity<InterestsActivity>()
  this.finish()
}

fun Activity.openLoadUserTypeActivity() {
  startActivity<LoadUserTypeActivity>()
  this.finish()
}

fun Activity.openUserMainActivity() {
  startActivity<UserMainActivity>()
  this.finish()
}

