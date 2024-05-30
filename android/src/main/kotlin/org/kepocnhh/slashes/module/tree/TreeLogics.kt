package org.kepocnhh.slashes.module.tree

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.slashes.module.app.Injection
import sp.kx.logics.Logics
import java.io.File

internal class TreeLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    data class State(
        val parent: File?,
        val current: File,
        val list: List<File>,
    )

    private val logger = injection.loggers.create("[Tree]")
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    fun requestState(current: File) = launch {
        logger.debug("request: ${current.absolutePath}")
        _state.value = withContext(injection.contexts.default) {
            check(current.exists())
            check(current.isDirectory)
            val comparator = Comparator<File> { left, right ->
                when {
                    left.isDirectory -> when {
                        right.isDirectory -> left.name.compareTo(right.name)
                        else -> -1
                    }
                    right.isDirectory -> 1
                    else -> left.name.compareTo(right.name)
                }
            }
            val list = current.listFiles()?.sortedWith(comparator).orEmpty()
            logger.debug("children: $list")
            State(
                parent = current.parentFile,
                current = current,
                list = list,
            )
        }
    }
}
