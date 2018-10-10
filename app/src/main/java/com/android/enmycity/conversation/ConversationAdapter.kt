package com.android.enmycity.conversation

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.android.enmycity.R

class ConversationAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ConversationAdapter.MessageViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_message, parent, false)
    return MessageViewHolder(view, parent.context)
  }

  override fun getItemCount(): Int = messages.size

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.bind(messages[position])
  }

  fun addMessage(message: Message) {
    messages.add(message)
  }

  inner class MessageViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
    private val ownerText = view.findViewById<TextView>(R.id.viewHolderMessage_ownerMessage_textView)
    private val ownerCardView = view.findViewById<CardView>(R.id.viewHolderMessage_ownerMessage_cardView)
    private val guestText = view.findViewById<TextView>(R.id.viewHolderMessage_guestMessage_textView)
    private val guestCardView = view.findViewById<CardView>(R.id.viewHolderMessage_guestMessage_cardView)
    fun bind(message: Message) {
      message.let {
        if (it.isOwner) showOwnerMessage(it) else showGuestMessage(it)
      }
    }

    private fun showOwnerMessage(message: Message) {
      ownerCardView.visibility = VISIBLE
      ownerText.text = message.message
      guestCardView.visibility = GONE
    }

    private fun showGuestMessage(message: Message) {
      guestCardView.visibility = VISIBLE
      guestText.text = message.message
      ownerCardView.visibility = GONE
    }
  }
}