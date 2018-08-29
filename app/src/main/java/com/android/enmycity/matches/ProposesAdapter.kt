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
import com.android.enmycity.common.StatusId
import com.bumptech.glide.Glide
import org.jetbrains.anko.toast

class ProposesAdapter(
    private val proposes: MutableList<ProposeViewModel>,
    private val context: Context
) : RecyclerView.Adapter<ProposesAdapter.ProposeViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProposeViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_propose, parent, false)
    return ProposeViewHolder(view)
  }

  override fun getItemCount(): Int = proposes.size

  override fun onBindViewHolder(holder: ProposeViewHolder, position: Int) {
    holder.bind(proposes[position])
  }

  fun addPropose(proposeViewModel: ProposeViewModel) {
    proposes.add(proposeViewModel)
  }

  inner class ProposeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
          setOnClickListener { context.toast("ACEPTADO") }
        }
      }
    }
  }
}


