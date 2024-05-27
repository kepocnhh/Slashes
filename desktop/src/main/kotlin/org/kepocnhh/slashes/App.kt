package org.kepocnhh.slashes

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.util.UUID

fun main() {
    application {
        Window(onCloseRequest = ::exitApplication) {
            val foo = remember { mutableStateOf(Foo(UUID.randomUUID())) }.value
            Bar(foo = foo)
        }
    }
}
