package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.gitScapeLog
import okhttp3.logging.HttpLoggingInterceptor

val loggingInterceptor = HttpLoggingInterceptor { message ->
    gitScapeLog(
        message = message,
        errorMessage = null,
        info = true
    )
}.apply {
    level = HttpLoggingInterceptor.Level.BODY
}