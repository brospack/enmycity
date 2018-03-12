package com.android.enmycity.search

import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.bumptech.glide.Glide

class ProfilesAdapter(private val elements: List<UserDao>, private val context: Context) :
    RecyclerView.Adapter<ProfilesAdapter.ProfilesView>() {

  override fun getItemCount() = elements.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilesView {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.view_user_profile, parent, false)
    return ProfilesView(view)
  }

  override fun onBindViewHolder(holder: ProfilesView, position: Int) {
    holder.bind(elements.get(position))
  }

  inner class ProfilesView(view: View) : RecyclerView.ViewHolder(view) {
    private val nameTextView by lazy { view.findViewById<TextView>(R.id.viewUserProfile_name) }
    private val avatarImageView by lazy { view.findViewById<ImageView>(R.id.viewUserProfile_avatar_imageView) }

    fun bind(profileViewModel: UserDao) {
      nameTextView.text = profileViewModel.name
      Glide.with(context).load(profileViewModel.photoUrl).into(avatarImageView)
    }
  }
}