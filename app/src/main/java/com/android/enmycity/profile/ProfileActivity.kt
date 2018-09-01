package com.android.enmycity.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.android.enmycity.R
import com.android.enmycity.data.UserDao
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.openSearchActivity
import com.android.enmycity.openUserMainActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.profile_avatar_imageView
import kotlinx.android.synthetic.main.activity_profile.profile_contact_floatingActionButton
import kotlinx.android.synthetic.main.activity_profile.profile_name_textView
import org.jetbrains.anko.toast

class ProfileActivity : AppCompatActivity(), ProfileView {
  private val presenter: ProfilePresenter by lazy {
    ProfilePresenter(UserSharedPreferences(this))
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
      presenter.onProposeCreated(profileViewModel)
    }

    supportActionBar?.let {
      it.setDisplayHomeAsUpEnabled(true)
      it.setDisplayShowHomeEnabled(true)
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

  override fun proposeSendedMessage() {
    toast("OK")
    openUserMainActivity()
  }

  override fun proposeDidntSended() {
    toast("KO")
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) {
      finish()
    }
    return super.onOptionsItemSelected(item)
  }
}
