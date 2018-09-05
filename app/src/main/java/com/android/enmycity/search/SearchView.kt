package com.android.enmycity.search

import com.android.enmycity.data.User

interface SearchView {
  fun showMessage(message: String)
  fun addProfile(user:User)
  fun showLocation(latitude: Double, longitude: Double)
  fun showProgressBar()
  fun hideProgressBar()
}