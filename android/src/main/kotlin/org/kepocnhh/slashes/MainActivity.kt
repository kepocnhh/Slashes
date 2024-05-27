package org.kepocnhh.slashes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import java.util.UUID

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ComposeView(this)
        setContentView(view)
        view.setContent {
            val foo = remember { mutableStateOf(Foo(UUID.randomUUID())) }.value
            Bar(foo = foo)
        }
    }
}
