package de.uriegel.fireplayer.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Arrays

val DEFAULT_BUFFER_SIZE= 8192

suspend fun InputStream.readAllAsync(): ByteArray =
    withContext(Dispatchers.IO) {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        while (true) {
            currentCoroutineContext().ensureActive()

            val n = read(buffer)

            if (n < 0)
                break

            if (n > 0)
                output.write(buffer, 0, n)
        }

        output.toByteArray()
    }

fun InputStream.readAll(): ByteArray {
    var bufs: MutableList<ByteArray>? = null
    var result: ByteArray? = null
    var total = 0
    var remaining = Int.MAX_VALUE
    var n: Int

    do {
        val buf = ByteArray(Math.min(remaining, DEFAULT_BUFFER_SIZE))
        var nread = 0

        // read to EOF which may read more or less than buffer size
        while (read(
                buf, nread,
                Math.min(buf.size - nread, remaining)
            ).also { n = it } > 0
        ) {
            nread += n
            remaining -= n
        }
        if (nread > 0) {
            total += nread
            if (result == null) {
                result = buf
            } else {
                if (bufs == null) {
                    bufs = ArrayList()
                    bufs.add(result)
                }
                bufs.add(buf)
            }
        }
    } while (n >= 0 && remaining > 0)
    if (bufs == null) {
        if (result == null)
            return ByteArray(0)

        return if (result.size == total)
            result
        else
            Arrays.copyOf(result, total)
    }
    result = ByteArray(total)
    var offset = 0
    remaining = total
    for (b in bufs) {
        val count = Math.min(b.size, remaining)
        System.arraycopy(b, 0, result, offset, count)
        offset += count
        remaining -= count
    }
    return result
}