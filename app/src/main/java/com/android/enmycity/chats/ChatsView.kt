package com.android.enmycity.chats

interface ChatsView {
  fun showChat(chat: Chat)
  fun hideProgressBar()
  fun showDeletedMessage()
  fun showDeleteDialogConfirmation()
}