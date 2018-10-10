package com.android.enmycity.chats

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import com.android.enmycity.R
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_messages.chat_chats_recyclerView
import kotlinx.android.synthetic.main.fragment_messages.chat_progressBar
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class ChatsFragment : Fragment(), ChatsView {
  private val presenter: ChatsPresenter by lazy {
    ChatsPresenter(FirebaseFirestore.getInstance(),
        UserSharedPreferences(context!!))
  }
  private var chatsAdapter = ChatsAdapter(mutableListOf())

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_messages, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.setView(this)
    presenter.onViewReady()
    initRecyclerView()
  }

  private fun initRecyclerView() {
    chat_chats_recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
      adapter = chatsAdapter
    }
  }

  override fun showChat(chat: Chat) {
    chatsAdapter.addChat(chat)
  }

  override fun hideProgressBar() {
    chat_progressBar.visibility = GONE
  }

  override fun showDeletedMessage() {
    activity?.toast("polla")
  }

  override fun showDeleteDialogConfirmation() {
    activity?.alert("Seguro que quieres?", "eliminar chat") {
      yesButton { presenter.onChatDeleted() }
      noButton { }
    }?.show()
  }

//  val dataBaseReference = FirebaseDatabase.getInstance().reference
//
//  val message = Message("12331UID", "test message", Date().time)
//  val key = dataBaseReference.child("chat").child("generatedChat2").push().key
//  dataBaseReference.child("chat").child("popo").setValue(key)
}