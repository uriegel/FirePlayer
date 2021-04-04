package de.uriegel.fireplayer

import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val pwdPref = findPreference<EditTextPreference>("auth_pw")
            if (pwdPref != null) {
                pwdPref.summaryProvider = Preference.SummaryProvider<Preference?> {
                    val getPassword: String =
                        PreferenceManager.getDefaultSharedPreferences(context).getString("auth_pw", "")!!

                    setAsterisks(getPassword.length)
                }

                // Set type as password and set summary with asterisks
                pwdPref.setOnBindEditTextListener { editText ->
                    editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    pwdPref.summaryProvider =
                        Preference.SummaryProvider<Preference> { setAsterisks(editText.text.toString().length) }
                }
            }
        }

        private fun setAsterisks(length: Int): String {
            val sb = java.lang.StringBuilder()
            for (s in 0 until length) {
                sb.append("*") }
            return sb.toString() }
    }
}