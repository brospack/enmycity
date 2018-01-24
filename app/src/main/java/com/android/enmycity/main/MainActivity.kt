package com.android.enmycity.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.enmycity.openLoginActivity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
  private val word = "Thread"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //If user is not user logged then open login activity
    this.openLoginActivity()
    finish()

//    Observable.just(1, 2, 3, 4, 5)
//        .doOnNext { Log.i(word, Thread.currentThread().name + 1) }
//        .map {
//          Log.i(word, Thread.currentThread().name + 2)
//          it * 2
//        }
//        .subscribeWith(object : DisposableObserver<Int>() {
//          override fun onComplete() {
//
//          }
//
//          override fun onNext(t: Int) {
//            Log.i(word, Thread.currentThread().name + 3)
//            Log.i("number", t.toString())
//          }
//
//          override fun onError(e: Throwable) {
//
//          }
//
//        })

//    val integerObservable = Observable.just(1, 2, 3, 4, 5, 6)

//    integerObservable
//        .doOnNext { Log.i(word, "Emitting item $it on ${Thread.currentThread().name}") }
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe { Log.i(word, "Consuming item $it on ${Thread.currentThread().name}") }

//    integerObservable
//        .doOnNext { Log.i(word, "Emitting item $it on ${Thread.currentThread().name}") }
//        .subscribeOn(Schedulers.io())
//        .observeOn(Schedulers.computation())
//        .map {
//          Log.i(word, "Mapping item $it on ${Thread.currentThread().name}")
//          it }
//        .observeOn(Schedulers.newThread())
//        .filter{Log.i(word, "Filtering item $it on ${Thread.currentThread().name}")
//          it %2 == 0 }
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe { Log.i(word, "Consuming item $it on ${Thread.currentThread().name}") }

  }

}