package com.manmadehorrorsstudio.gitscape

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

fun gitScapeLog(message: String, errorMessage: Throwable? = null) {
    val GITSCAPEDEBUGTAG = "GITSCAPEDEBUGTAG"
    if (!BuildConfig.BUILD_TYPE.equals(BuildConfig.RELEASE_SIGNING_CONFIG, true))
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

fun openBrowser(urlString: String?, context: Context?) {
    urlString?.let { url ->
        context?.let { ctx ->
            val intent = Intent(
                Intent.ACTION_VIEW,
                url.toUri()
            )
            ctx.startActivity(intent)
        } ?: gitScapeLog("openBrowser", Exception("Missing context"))
    } ?: gitScapeLog("openBrowser", Exception("Missing url"))
}