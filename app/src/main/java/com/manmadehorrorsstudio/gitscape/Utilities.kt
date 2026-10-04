package com.manmadehorrorsstudio.gitscape

import android.util.Log

fun gitScapeLog(message: String, errorMessage: Exception? = null) {
    val GITSCAPEDEBUGTAG = "GITSCAPEDEBUGTAG"
    //if (!BuildConfig.BUILD_TYPE.equals(BuildConfig.RELEASE_SIGNING_CONFIG, true))
        errorMessage?.message?.letNotEmpty{ em ->
            Log.e(GITSCAPEDEBUGTAG, "$message\n$em")
        } ?: run {
            Log.d(GITSCAPEDEBUGTAG, message)
        }
}

inline fun <R> String?.letNotEmpty(block: (String) -> R): R? {
    return if (this.isNullOrEmpty()) {
        null
    } else {
        block(this)
    }
}