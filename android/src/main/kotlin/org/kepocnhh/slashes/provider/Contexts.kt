package org.kepocnhh.slashes.provider

import kotlin.coroutines.CoroutineContext

internal class Contexts(
    val main: CoroutineContext,
    val default: CoroutineContext,
)
