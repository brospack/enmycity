package com.android.enmycity.search

import com.android.enmycity.data.UserDao

interface SearchView {
  fun showMessage(message: String)
  fun addProfiles(profiles: List<UserDao>)
  fun showLocation(latitude: Double, longitude: Double)
  fun showProgressBar()
  fun hideProgressBar()
}