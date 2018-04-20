package com.android.enmycity.services

import com.android.enmycity.data.DeviceSharedPreferences
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIdService : FirebaseInstanceIdService() {
  private val deviceSharedPreferences: DeviceSharedPreferences by lazy { DeviceSharedPreferences(this) }
  override fun onTokenRefresh() {
    super.onTokenRefresh()
    FirebaseInstanceId.getInstance()?.token?.let {
      deviceSharedPreferences.saveToken(it)
    }
  }
}