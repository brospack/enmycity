package com.android.enmycity

import android.content.Context
import android.content.Intent
import com.android.enmycity.interests.InterestsActivity
import com.android.enmycity.user.SelectTypeUserActivity
import org.jetbrains.anko.startActivity

fun Context.openLoginActivity() = startActivity<UserLoginActivity>()
fun Context.openInterestsActivity() = startActivity<InterestsActivity>()



