package com.android.enmycity.chats

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.enmycity.R
import com.android.enmycity.conversation.ConversationActivity
import com.bumptech.glide.Glide

class ChatsAdapter(private val chats: MutableList<Chat>) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_chat, parent, false)
    return ChatViewHolder(view, parent.context)
  }

  override fun getItemCount(): Int = chats.size

  override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
    holder.bind(chats[position])
  }

  fun addChat(chat: Chat) {
    chats.add(chat)
  }

  inner class ChatViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
    private val photo = view.findViewById<ImageView>(R.id.viewHolderChat_avatar_imageView)
    private val name = view.findViewById<TextView>(R.id.viewHolderChat_name_textView)

    fun bind(chat: Chat) {
      name.text = chat.name
      Glide.with(context).load(chat.photo).into(photo)
      itemView.setOnClickListener { ConversationActivity.open(context, chat.id) }
    }
  }
}