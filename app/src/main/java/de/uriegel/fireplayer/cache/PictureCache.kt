package de.uriegel.fireplayer.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.bind
import de.uriegel.fireplayer.requests.getResponseStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PictureCache(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val items: List<String>,
    private val bitmap: MutableState<Bitmap>) {

    fun next() {
        val bitmapState = this.bitmap
        coroutineScope.launch {
            bitmapState.value =loadBitmap(++index)
        }
    }

    private suspend fun loadBitmap(index: Int): Bitmap =
        withContext(Dispatchers.IO) {
            return@withContext getResponseStream(items[index])
                .bind {
                    it.jpgToTempFile()
                }
                .fold({
                    try {
                        BitmapFactory.decodeStream(it.inputStream())
                    } finally {
                        it.delete()
                    }
                }, {
                    // Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    BitmapFactory.decodeResource(context.resources, R.drawable.emptypics)
                })
        }

    init {
        next()
    }

    private var index = -1
}