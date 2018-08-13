package com.android.enmycity.chats

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.enmycity.R
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ChatsFragment : Fragment(), ChatsView {
  private val presenter: ChatsPresenter by lazy {
    ChatsPresenter(FirebaseFirestore.getInstance(), FirebaseDatabase.getInstance(),
        UserSharedPreferences(context!!))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_messages, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
  }

  override fun showChats(chats: List<ChatDto>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

//  val dataBaseReference = FirebaseDatabase.getInstance().reference
//
//  val message = Message("12331UID", "test message", Date().time)
//  val key = dataBaseReference.child("chat").child("generatedChat2").push().key
//  dataBaseReference.child("chat").child("popo").setValue(key)
}