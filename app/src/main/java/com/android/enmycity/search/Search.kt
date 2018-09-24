package com.android.enmycity.search

data class Search(
    val placeId: String = "",
    val collection: String = "",
    val location: String = ""
) {
  fun isEmpty() = placeId.isEmpty()
  fun isNotEmpty() = !isEmpty()
}