package com.android.enmycity

import android.app.Activity

import com.android.enmycity.interests.InterestsActivity
import com.android.enmycity.search.SearchActivity
import com.android.enmycity.user.SelectTypeUserActivity
import org.jetbrains.anko.startActivity

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
  finish()
}

fun Activity.openSearchActivity() {
  startActivity<SearchActivity>()
  this.finish()
}

fun Activity.openLoadUserTypeActivity() {
  startActivity<LoadUserTypeActivity>()
  this.finish()
}


