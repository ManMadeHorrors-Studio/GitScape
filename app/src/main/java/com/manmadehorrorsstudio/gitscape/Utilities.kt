package com.manmadehorrorsstudio.gitscape

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.ui.models.LlmLogos

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

fun String.capitalizeFirst(): String = replaceFirstChar { character ->
    character.uppercase()
}

fun Long?.isNotExpired(): Boolean {
    return this?.let {
        it > (System.currentTimeMillis() / 1000)
    } == true
}

fun Long?.addCurrentTime(safetyTime: Long): Long? = this?.let { (System.currentTimeMillis() / 1000) + it - safetyTime }

@DrawableRes
fun LlmConfiguration?.getLogo(): Int =
    LlmLogos.entries.firstOrNull{ logo ->
        logo.id.equals(this?.llmProvider?.id, true)
    }?.logo
        ?: R.drawable.ai_icon

fun ContentDrawScope.drawGlowBorder(
    isHighlighted: Boolean,
    progressValue: Float,
    colors: ColorScheme
) {
    drawContent()

    if (isHighlighted) {
        val width = size.width
        val height = size.height
        val glowWidth = width * 0.35f
        val stroke = 2.dp.toPx()
        val x = progressValue * (width + height * 2)

        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    colors.primary,
                    colors.secondary,
                    Color.Transparent
                ),
                start = Offset(
                    x - glowWidth,
                    0f
                ),
                end = Offset(
                    x,
                    0f
                )
            ),
            size = size,
            cornerRadius = CornerRadius(
                32f,
                32f
            ),
            style = Stroke(
                width = stroke
            )
        )
    }
}