package de.uriegel.fireplayer.extensions

import android.content.Context
import android.content.ContextWrapper

fun Context.findActivityEx(): ComponentExActivity? =
    when (this) {
        is ComponentExActivity -> this
        is ContextWrapper -> baseContext.findActivityEx()
        else                    -> null
    }