package com.android.enmycity.search

import com.android.enmycity.data.User

interface SearchView {
  fun showMessage(message: String)
  fun showPlaceAutocompleteEmpty()
  fun showPlaceAutocompleteWithText(location: String)
  fun clearProfiles()
  fun addProfile(user: User)
  fun showProgressBar()
  fun hideProgressBar()
}