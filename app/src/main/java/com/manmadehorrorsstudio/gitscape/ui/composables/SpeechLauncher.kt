package com.manmadehorrorsstudio.gitscape.ui.composables

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.models.AppLanguage

@Composable
fun speechLauncher(
    onReadedText:(String) -> Unit,
    hint: String = localizedString(R.string.SEARCH_HINT_GENERIC),
    languageTag: String = AppLanguage.EN.locale.toLanguageTag()
): () -> Unit {

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val spokenText = result.data
                ?.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )
                ?.firstOrNull()

            spokenText?.let { st ->
                gitScapeLog("Voice search: $st")
                onReadedText(st)
            }
        }
    }

    return {
        val speechIntent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )

                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    languageTag
                )

                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    hint
                )
            }

        speechLauncher.launch(speechIntent)
    }
}