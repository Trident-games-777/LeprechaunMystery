package com.dsfgland.goa.util.ext

import android.app.Activity
import android.content.Intent

inline fun <reified T> Activity.start() {
    startActivity(Intent(this, T::class.java))
    finish()
}

inline fun <reified T> Activity.start(url: String) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(URL_KEY, url)
    startActivity(intent)
    finish()
}