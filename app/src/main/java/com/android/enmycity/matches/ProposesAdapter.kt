package com.android.enmycity.matches

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.enmycity.R
import com.android.enmycity.common.FirestoreCollectionNames
import com.android.enmycity.common.StatusId
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.toast

class ProposesAdapter(
    private val proposes: MutableList<ProposeViewModel>
) : RecyclerView.Adapter<ProposesAdapter.ProposeViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProposeViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_propose, parent, false)
    return ProposeViewHolder(view, parent.context)
  }

  override fun getItemCount(): Int = proposes.size

  override fun onBindViewHolder(holder: ProposeViewHolder, position: Int) {
    holder.bind(proposes[position])
  }

  fun addPropose(proposeViewModel: ProposeViewModel) {
    proposes.add(proposeViewModel)
  }

  inner class ProposeViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
    private val avatar by lazy { view.findViewById<ImageView>(R.id.viewPropose_avatar_imageView) }
    private val name by lazy { view.findViewById<TextView>(R.id.viewPropose_name_textView) }
    private val status by lazy { view.findViewById<TextView>(R.id.viewPropose_status_textView) }
    private val acceptPropose by lazy { view.findViewById<Button>(R.id.viewPropose_accept_button) }

    fun bind(proposeViewModel: ProposeViewModel) {
      name.text = proposeViewModel.userName
      Glide.with(context).load(proposeViewModel.userPhoto).into(avatar)
      if (proposeViewModel.isOwner) {
        status.apply {
          visibility = VISIBLE
          text = "Pendiente"
        }
      } else {
        acceptPropose.apply {
          visibility = VISIBLE
          setOnClickListener { createConversation(proposeViewModel.chatId) }
        }
      }
    }

    private fun createConversation(chatId: String) {
      FirebaseFirestore.getInstance()
          .collection(FirestoreCollectionNames.CHATS)
          .document(chatId)
          .update("status", 2)
          .addOnSuccessListener { addConversationToRealtimedb(chatId) }
          .addOnFailureListener { context.toast("Ha ocurrido un error") }
    }

    private fun addConversationToRealtimedb(chatId: String) {
      FirebaseDatabase.getInstance()
          .reference
          .child("conversations")
          .setValue(chatId)
          .addOnSuccessListener { }
          .addOnFailureListener { context.toast("Ha ocurrido un error") }
    }
  }
}


