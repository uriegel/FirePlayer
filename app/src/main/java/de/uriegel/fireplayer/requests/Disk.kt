package de.uriegel.fireplayer.requests

suspend fun accessDisk() = getString("/accessdisk")
