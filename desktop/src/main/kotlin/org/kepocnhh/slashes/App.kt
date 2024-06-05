package org.kepocnhh.slashes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.slashes.module.app.Injection
import org.kepocnhh.slashes.module.tree.TreeScreen
import org.kepocnhh.slashes.provider.Contexts
import org.kepocnhh.slashes.provider.FinalLoggers
import sp.kx.logics.Logics
import sp.kx.logics.LogicsFactory
import sp.kx.logics.LogicsProvider
import sp.kx.logics.contains
import sp.kx.logics.get
import sp.kx.logics.remove

fun main() {
    println("version: ${App.VERSION}") // todo
    application {
        Window(onCloseRequest = ::exitApplication) {
            TreeScreen()
        }
    }
}

internal object App {
    const val VERSION = 1

    private var _injection: Injection? = null

    private val _logicsProvider = LogicsProvider(
        factory = object : LogicsFactory {
            override fun <T : Logics> create(type: Class<T>): T {
                val injection = checkNotNull(_injection) { "No injection!" }
                return type
                    .getConstructor(Injection::class.java)
                    .newInstance(injection)
            }
        },
    )

    init {
        _injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                default = Dispatchers.Default,
            ),
            loggers = FinalLoggers(),
        )
    }

    @Composable
    inline fun <reified T : Logics> logics(label: String = T::class.java.name): T {
        val (contains, logics) = synchronized(App::class.java) {
            remember { _logicsProvider.contains<T>(label = label) } to _logicsProvider.get<T>(label = label)
        }
        DisposableEffect(Unit) {
            onDispose {
                if (!contains) _logicsProvider.remove<T>(label = label)
            }
        }
        return logics
    }
}
