package com.android.enmycity.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View.GONE
import android.widget.LinearLayout
import com.android.enmycity.R
import com.android.enmycity.data.UserSharedPreferences
import kotlinx.android.synthetic.main.activity_conversation.conversation_message_editText
import kotlinx.android.synthetic.main.activity_conversation.conversation_messages_recyclerView
import kotlinx.android.synthetic.main.activity_conversation.conversation_progressBar
import kotlinx.android.synthetic.main.activity_conversation.conversation_sendMessage_floatingActionButton

class ConversationActivity : AppCompatActivity(), ConversationView {
  override fun hideLoading() {
    conversation_progressBar.visibility = GONE
  }

  companion object {
    private const val CHAT_ID_EXTRA = "CONVERSATION_ACTIVITY_CHAT_ID_EXTRA"
    fun open(context: Context, chatId: String) {
      val intent = Intent(context, ConversationActivity::class.java).apply {
        putExtra(CHAT_ID_EXTRA, chatId)
      }
      context.startActivity(intent)
    }
  }

  private val presenter by lazy {
    ConversationPresenter(UserSharedPreferences(this))
  }
  private val conversationAdapter = ConversationAdapter(mutableListOf())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_conversation)
    enableBackArrow()
    initRecyclerView()
    val chatId = intent.extras.getString(CHAT_ID_EXTRA, "")
    presenter.setView(this)
    presenter.onConversationReady(chatId)

    conversation_sendMessage_floatingActionButton.setOnClickListener {
      presenter.onMessageSent(conversation_message_editText.text.toString())
    }
  }

  private fun enableBackArrow() {
    supportActionBar?.let {
      it.setDisplayHomeAsUpEnabled(true)
      it.setDisplayShowHomeEnabled(true)
    }
  }

  private fun initRecyclerView() {
    conversation_messages_recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
      adapter = conversationAdapter
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun showAllMessages(messages: List<Message>) {
    conversationAdapter.addMessages(messages)
  }

  override fun showMessage(message: Message) {
    conversationAdapter.addMessage(message)
    conversationAdapter.notifyDataSetChanged()
  }

  override fun deleteTypedMessage() {
    conversation_message_editText.text.clear()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onViewClosed()
  }
}