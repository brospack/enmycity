package com.android.enmycity.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.enmycity.R
import com.android.enmycity.data.User
import com.android.enmycity.data.UserDao
import com.android.enmycity.profile.ProfileActivity
import com.android.enmycity.profile.ProfileViewModel
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject

class ProfilesAdapter(private val elements: List<User>, private val context: Context) :
    RecyclerView.Adapter<ProfilesAdapter.ProfilesView>() {

  val itemClickStream: PublishSubject<View> = PublishSubject.create()
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

    fun bind(user: User) {
      nameTextView.text = user.name
      Glide.with(context).load(user.photoUrl).into(avatarImageView)
      val profileViewModel = with(user) {
        ProfileViewModel(name, coffeeLanguage, nightLife, localShopping, gastronomicTour, cityTour, sportBreak, volunteering, photoUrl,
            email, id)
      }
      itemView.setOnClickListener { ProfileActivity.open(profileViewModel, context) }
    }
  }
}