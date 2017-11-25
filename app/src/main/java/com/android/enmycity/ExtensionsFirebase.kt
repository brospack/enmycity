package com.android.enmycity

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

fun Context.addFirebaseEvent(name: String, detail: String, message: String) {
  val eventDetails = Bundle().apply {
    putString(message, detail)
  }
  FirebaseAnalytics.getInstance(this).logEvent(name, eventDetails)
}