package com.android.enmycity.conversation

interface ConversationView {
  fun showMessage(message: Message)
  fun hideLoading()
  fun deleteTypedMessage()
}