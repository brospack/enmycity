package com.android.enmycity.conversation

interface ConversationView {
  fun showAllMessages(messages: List<Message>)
  fun showMessage(message: Message)
  fun hideLoading()
  fun deleteTypedMessage()
}