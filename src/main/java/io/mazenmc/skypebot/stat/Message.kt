package io.mazenmc.skypebot.stat

public data class Message(var contents: String, var time: Long) {
    public fun contents(): String {
        return contents
    }

    public fun time(): Long {
        return time
    }
}
