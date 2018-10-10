package com.android.enmycity.chats

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.enmycity.R
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.conversation.ConversationActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class ChatsAdapter(private val chats: MutableList<Chat>) : RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_chat, parent, false)
    return ChatViewHolder(view, parent.context)
  }

  override fun getItemCount(): Int = chats.size

  override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
    holder.bind(chats[position], position)
  }

  fun addChat(chat: Chat) {
    chats.add(chat)
    notifyDataSetChanged()
  }

  private fun deleteChat(chatId: String, position: Int) {
    FirebaseFirestore.getInstance()
        .collection(FirestoreCollectionNames.CHATS)
        .document(chatId)
        .delete()
        .addOnSuccessListener { pullChat(position) }
  }

  private fun pullChat(position: Int) {
    chats.removeAt(position)
    notifyDataSetChanged()
  }

  inner class ChatViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
    private val photo = view.findViewById<ImageView>(R.id.viewHolderChat_avatar_imageView)
    private val name = view.findViewById<TextView>(R.id.viewHolderChat_name_textView)
    private val deleteButton = view.findViewById<Button>(R.id.viewHolderChat_delete_button)

    fun bind(chat: Chat, position: Int) {
      name.text = chat.name
      Glide.with(context).load(chat.photo).into(photo)
      itemView.setOnClickListener { ConversationActivity.open(context, chat.id) }
      deleteButton.setOnClickListener { deleteChat(chat.id, position) }
    }
  }
}