package de.uriegel.fireplayer.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.InputType
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import de.uriegel.fireplayer.ComponentExActivity

fun Context.setPasswordBehavior(preference: EditTextPreference, key: String) {

    fun setBullets(length: Int): String {
        val sb = java.lang.StringBuilder()
        for (s in 0 until length) {
            sb.append("*") }
        return sb.toString() }

    val password = PreferenceManager.getDefaultSharedPreferences(this).getString(key, "")!!

    preference.summaryProvider = Preference.SummaryProvider<Preference?> {
        setBullets(if (password.isNotEmpty()) 10 else 0)
    }

    preference.setOnBindEditTextListener { editText ->
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        preference.summaryProvider =
            Preference.SummaryProvider<Preference> {
                setBullets(if (editText.text.toString().isNotEmpty()) 10 else 0)
            }
    }
}

fun Context.findActivity(): Activity? =
    when (this) {
        is Activity       -> this
        is ContextWrapper -> baseContext.findActivity()
        else              -> null
}

fun Context.findActivityEx(): ComponentExActivity? =
    when (this) {
        is ComponentExActivity  -> this
        is ContextWrapper       -> baseContext.findActivityEx()
        else                    -> null
    }

fun Context.addWindowFlags(flags: Int) {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    window.addFlags(flags)
}

fun Context.clearWindowFlags(flags: Int) {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    window.clearFlags(flags)
}

fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}