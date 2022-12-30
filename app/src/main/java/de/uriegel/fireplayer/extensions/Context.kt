package de.uriegel.fireplayer.extensions

import android.content.Context
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager

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