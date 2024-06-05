package org.kepocnhh.slashes.provider

interface Loggers {
    fun create(tag: String): Logger
}

interface Logger {
    fun debug(message: String)
}
