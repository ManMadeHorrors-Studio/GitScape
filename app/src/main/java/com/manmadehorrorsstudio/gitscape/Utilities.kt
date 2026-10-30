package com.manmadehorrorsstudio.gitscape

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

fun gitScapeLog(
    message: String,
    errorMessage: Throwable? = null,
    info: Boolean = false
) {
    val GITSCAPEDEBUGTAG = "GITSCAPEDEBUGTAG"
    if (!BuildConfig.BUILD_TYPE.equals(BuildConfig.RELEASE_SIGNING_CONFIG, true))
        errorMessage?.message?.letNotEmpty{ em ->
            Log.e(GITSCAPEDEBUGTAG, "$message\n$em")
        } ?: run {
            if(info)
                Log.i(GITSCAPEDEBUGTAG, message)
            else
                Log.d(GITSCAPEDEBUGTAG, message)
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

inline fun <R> String?.letNotEmpty(block: (String) -> R): R? {
    return if (this.isNullOrEmpty()) {
        null
    } else {
        block(this)
    }
}

fun Long?.isNotExpired(): Boolean {
    return this?.let {
        it > (System.currentTimeMillis() / 1000)
    } == true
}

fun Long?.addCurrentTime(safetyTime: Long): Long? = this?.let { (System.currentTimeMillis() / 1000) + it - safetyTime }