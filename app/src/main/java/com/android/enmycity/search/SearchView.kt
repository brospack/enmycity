package com.android.enmycity.search

import com.android.enmycity.data.User

interface SearchView {
  fun showMessage(message: String)
  fun addProfiles(profiles: List<User>)
  fun showLocation(latitude: Double, longitude: Double)
  fun showProgressBar()
  fun hideProgressBar()
}