package com.android.enmycity.matches

interface ProposeView {
  fun showPropose(propose: ProposeViewModel)
  fun showEmptyData()
  fun showLoading()
  fun hideLoading()
}