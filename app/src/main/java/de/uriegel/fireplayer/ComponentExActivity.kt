package de.uriegel.fireplayer

import android.view.KeyEvent
import androidx.activity.ComponentActivity

open class ComponentExActivity : ComponentActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent?) =
        if (!keyEvent(keyCode, event))
            super.onKeyDown(keyCode, event)
        else
            true

    var keyEvent = { _: Int, _: KeyEvent? -> false }
}