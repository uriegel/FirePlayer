package de.uriegel.fireplayer.cache

import kotlinx.coroutines.CoroutineScope

class PictureCache(private val coroutineScope: CoroutineScope, private val items: List<String>) {

//    @OptIn(DelicateCoroutinesApi::class)
//    fun cache() {
//        val okMap = map.filter { it.key.isInRange(index) }
//        if (okMap.size < size) {
//            val start = index + okMap.size
//            coroutineScope.launch {
//                if (start < items.size) {
//                    val file = getResponseStream(newSingleThreadContext("MyOwnThread"), items[start])
//                        .bind { it.jpgToTempFile() }
//                    file.fold({
//                        val okMapNew = map.filter { it.key >= index && it.key < index + 10 }
//                        if (okMapNew.size < size) {
//                            if (start.isInRange(index)) {
//                                map = okMapNew + mapOf(start to it)
//                            }
//                        }
//                    }, {})
//                }
//            }
//        }
//    }

    private var index = 0
}