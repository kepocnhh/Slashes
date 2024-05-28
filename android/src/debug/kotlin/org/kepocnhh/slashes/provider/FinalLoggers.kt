package org.kepocnhh.slashes.provider

import android.util.Log

internal class FinalLoggers : Loggers {
    override fun create(tag: String): Logger {
        return AndroidLogger(tag = tag)
    }
}

private class AndroidLogger(
    private val tag: String,
) : Logger {
    override fun debug(message: String) {
        Log.d(tag, message)
    }
}
