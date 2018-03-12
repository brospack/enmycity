package com.android.enmycity.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.android.enmycity.BuildConfig
import com.android.enmycity.R
import com.android.enmycity.data.UserSharedPreferences
import com.android.enmycity.openLoginActivity
import com.android.enmycity.openSearchActivity
import com.android.enmycity.openSelectUserTypeActivity
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {
  private val userSharedPreferences: UserSharedPreferences by lazy { UserSharedPreferences(this) }
  private val locationManager: LocationManager by lazy { this.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

  companion object {
    private const val REQUEST_CODE_SETTINGS = 1
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setLocationPermission()
  }

  private fun setLocationPermission() {
    Dexter.withActivity(this)
        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(object : PermissionListener {
          override fun onPermissionGranted(response: PermissionGrantedResponse?) {
            checkIfLocationIsActive()
          }

          override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
            token?.continuePermissionRequest()
          }

          override fun onPermissionDenied(response: PermissionDeniedResponse?) {
            showSnack(R.string.userLogin_permission_denied_advice_messa,
                R.string.userlogin_accept_permission_button, { goToPermissions() })
          }
        }).check()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_SETTINGS) {
      checkIfLocationIsActive()
    }
  }

  @SuppressLint("MissingPermission")
  private fun checkIfLocationIsActive() {
    val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    if (location != null) {
      when (isUserLogged()) {
        true -> getUserData()
        false -> openLoginActivity()
      }
    } else {
      showsLocationsDialog()
    }
  }

  private fun showsLocationsDialog() {
    alert(R.string.userLogin_allow_location_message) {
      positiveButton(R.string.accept, { openSettings() })
      negativeButton(R.string.cancel, {
        showSnack(R.string.userlogin_open_settings_message,
            R.string.userlogin_open_settings_button,
            { openSettings() })
      })
    }.show()
  }

  private fun openSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    startActivityForResult(intent, REQUEST_CODE_SETTINGS)
  }

  private fun goToPermissions() {
    Intent().apply {
      action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
      data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
      startActivityForResult(this, REQUEST_CODE_SETTINGS)
    }
  }

  private fun showSnack(message: Int, actionMessage: Int, onClickListener: (View) -> Unit) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionMessage, onClickListener)
        .show()
  }

  private fun isUserLogged() = FirebaseAuth.getInstance().currentUser != null

  private fun getUserData() = if (userSharedPreferences.isUserLoaded()) openSearchActivity() else openSelectUserTypeActivity()

  private fun observableTest() {
    val word = "Thread"
    Observable.just(1, 2, 3, 4, 5)
        .doOnNext { Log.i(word, Thread.currentThread().name + 1) }
        .map {
          Log.i(word, Thread.currentThread().name + 2)
          it * 2
        }
        .subscribeWith(object : DisposableObserver<Int>() {
          override fun onComplete() {

          }

          override fun onNext(t: Int) {
            Log.i(word, Thread.currentThread().name + 3)
            Log.i("number", t.toString())
          }

          override fun onError(e: Throwable) {

          }

        })

    val integerObservable = Observable.just(1, 2, 3, 4, 5, 6)

    integerObservable
        .doOnNext { Log.i(word, "Emitting item $it on ${Thread.currentThread().name}") }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { Log.i(word, "Consuming item $it on ${Thread.currentThread().name}") }

    integerObservable
        .doOnNext { Log.i(word, "Emitting item $it on ${Thread.currentThread().name}") }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .map {
          Log.i(word, "Mapping item $it on ${Thread.currentThread().name}")
          it
        }
        .observeOn(Schedulers.newThread())
        .filter {
          Log.i(word, "Filtering item $it on ${Thread.currentThread().name}")
          it % 2 == 0
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { Log.i(word, "Consuming item $it on ${Thread.currentThread().name}") }
  }
}