package com.android.enmycity.conversation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.enmycity.R

class ConversationAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<ConversationAdapter.MessageViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_message, parent, false)
    return MessageViewHolder(view)
  }

  override fun getItemCount(): Int = messages.size

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.bind(messages[position])
  }

  fun addMessage(message: Message) {
    messages.add(message)
  }

  inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val text = view.findViewById<TextView>(R.id.viewHolderMessage_message)
    fun bind(message: Message) {
      text.text = message.message
    }
  }
}