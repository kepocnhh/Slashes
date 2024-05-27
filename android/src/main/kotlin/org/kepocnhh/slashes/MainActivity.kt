package org.kepocnhh.slashes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import org.kepocnhh.slashes.module.router.RouterScreen
import org.kepocnhh.slashes.module.tree.TreeScreen

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ComposeView(this)
        setContentView(view)
        view.setContent {
            val isTreeState = remember { mutableStateOf(false) }
            if (isTreeState.value) {
                TreeScreen()
            } else {
                RouterScreen(
                    toTree = {
                        isTreeState.value = true
                    },
                )
            }
        }
    }
}
