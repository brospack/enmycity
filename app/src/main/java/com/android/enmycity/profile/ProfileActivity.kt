package com.android.enmycity.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profile.profile_avatar_imageView
import kotlinx.android.synthetic.main.activity_profile.profile_contact_floatingActionButton
import kotlinx.android.synthetic.main.activity_profile.profile_name_textView

class ProfileActivity : AppCompatActivity(), ProfileView {
  private val presenter: ProfilePresenter by lazy {
    ProfilePresenter(FirebaseDatabase.getInstance(),
        UserSharedPreferences(this))
  }
  private lateinit var profileViewModel: ProfileViewModel

  companion object {
    const val USER_KEY = "PARCELABLE_USER_KEY"
    fun open(profile: ProfileViewModel, context: Context) {
      val intent = Intent(context, ProfileActivity::class.java)
      intent.putExtra(USER_KEY, profile)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)
    presenter.setView(this)
    profileViewModel = intent.getParcelableExtra(USER_KEY)
    with(profileViewModel) {
      showImage(photoUrl)
      showName(name)
    }
    profile_contact_floatingActionButton.setOnClickListener {
      presenter.onProposeCreated(profileViewModel.email)
    }
  }

  private fun showImage(url: String) {
    Glide.with(this).load(url).into(profile_avatar_imageView)
  }

  private fun showName(name: String) {
    profile_name_textView.text = name
  }

  override fun showData(userDao: UserDao) {
  }
}
