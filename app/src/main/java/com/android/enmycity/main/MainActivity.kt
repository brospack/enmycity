package com.android.enmycity.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.enmycity.openLoginActivity

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //If user is not user logged then open login activity
    this.openLoginActivity()
    finish()
  }
}