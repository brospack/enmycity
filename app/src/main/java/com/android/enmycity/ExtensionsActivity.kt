package com.android.enmycity

import android.app.Activity
import android.content.Context
import com.android.enmycity.interests.InterestsActivity
import org.jetbrains.anko.startActivity

fun Context.openLoginActivity() = startActivity<UserLoginActivity>()
fun Context.openInterestsActivity() = startActivity<InterestsActivity>()


