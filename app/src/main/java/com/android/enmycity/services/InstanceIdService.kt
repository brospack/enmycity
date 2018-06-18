package com.android.enmycity.services

import com.google.firebase.iid.FirebaseInstanceIdService

class InstanceIdService : FirebaseInstanceIdService() {
  private val saveTokenUseCase: SaveTokenUseCase by lazy { SaveTokenUseCase(this) }
  override fun onTokenRefresh() {
    super.onTokenRefresh()
    saveTokenUseCase.saveToken()
  }
}