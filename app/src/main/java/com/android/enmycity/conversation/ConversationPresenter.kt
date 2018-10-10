package com.android.enmycity.conversation

import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConversationPresenter(private val userSharedPreferences: UserSharedPreferences) {
  private lateinit var view: ConversationView
  private lateinit var dbReference: DatabaseReference
  private lateinit var conversationValueEventListener: ValueEventListener
  private lateinit var conversationChildEventListener: ChildEventListener
  private var childEventListenerIsEnabled = false

  fun setView(view: ConversationView) {
    this.view = view
  }

  fun onConversationReady(chatId: String) {
//    createConversation(chatId)
    dbReference = FirebaseDatabase.getInstance().reference.child("conversations").child(chatId).ref

    conversationValueEventListener = object : ValueEventListener {
      override fun onCancelled(databaseError: DatabaseError) {
      }

      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot.children.forEach {
          showMessage(it)
        }
        setChildEventListener()
        view.hideLoading()
      }
    }
    dbReference.limitToLast(30).addListenerForSingleValueEvent(conversationValueEventListener)
  }

  private fun setChildEventListener() {
    conversationChildEventListener = object : ChildEventListener {
      override fun onCancelled(p0: DatabaseError) {
      }

      override fun onChildMoved(p0: DataSnapshot, p1: String?) {
      }

      override fun onChildChanged(p0: DataSnapshot, p1: String?) {
      }

      override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
        if (childEventListenerIsEnabled) showMessage(dataSnapshot)
      }

      override fun onChildRemoved(p0: DataSnapshot) {
      }
    }
    dbReference.addChildEventListener(conversationChildEventListener)
  }

  fun onMessageSent(text: String) {
    childEventListenerIsEnabled = true
    dbReference.push().key?.let {
      val message = MessageDto(userSharedPreferences.getUserLogged().id, text)
      dbReference.child(it).setValue(message)
      view.deleteTypedMessage()
    }
  }

  fun onViewClosed() {
    dbReference.removeEventListener(conversationChildEventListener)
  }

  private fun showMessage(dataSnapshot: DataSnapshot) {
    dataSnapshot.getValue(MessageDto::class.java)?.let {
      val userLoggedId = userSharedPreferences.getUserLogged().id
      val message = Message(it.userId, it.message, it.userId == userLoggedId)
      view.showMessage(message)
      view.hideLoading()
    }
  }

  private fun createConversation(chatId: String) {
    val alexId = "11111"
    val lauraId = "22222"
    val message1 = MessageDto(alexId, "Hola Laura")
    val message2 = MessageDto(lauraId, "Hola Alex, qué tal?")
    val conversationDto = ConversationDto(ownerId = alexId,
        ownerName = "Alex",
        ownerPhoto = "photo alex",
        guestId = lauraId,
        guestName = "Laura",
        guestPhoto = "photo laura",
        messages = listOf(message1, message2))

    val chatReference = FirebaseDatabase.getInstance().reference.child("conversations").child(chatId)

    chatReference.push().key?.let {
      chatReference.child(it).setValue(message1)
    }

    chatReference.push().key?.let {
      chatReference.child(it).setValue(message2)
    }
  }
}