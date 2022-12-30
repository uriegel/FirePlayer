package de.uriegel.fireplayer.requests

//@ExperimentalSerializationApi
suspend fun accessDisk() {
    getString("/accessdisk")
        .fold({
            val affe = it
        }, {
            val bitch = it
        })

}