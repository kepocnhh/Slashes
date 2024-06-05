package org.kepocnhh.slashes.module.tree

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import org.kepocnhh.slashes.App
import org.kepocnhh.slashes.util.toPaddings
import java.io.File

@Composable
private fun TreeScreen(
    state: TreeLogics.State,
    toParent: () -> Unit,
    toDir: (File) -> Unit,
) {
    val lazyListState = remember(key1 = state) {
        LazyListState(0, 0)
    }
    val insets = LocalView.current.rootWindowInsets.toPaddings()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = insets.calculateTopPadding()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            val parent = state.parent
            val enabled = parent != null && parent.canRead()
            BasicText(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable(enabled = enabled, onClick = toParent)
                    .wrapContentHeight()
                    .padding(16.dp),
                text = "<",
            )
            BasicText(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                text = state.current.absolutePath,
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = insets.calculateBottomPadding()),
            state = lazyListState,
        ) {
            state.list.forEach { file ->
                item(key = file.absolutePath) {
                    val isDirectory = file.isDirectory
                    val text = if (isDirectory) {
                        "Dir: " + file.name
                    } else {
                        file.name
                    }
                    val enabled = file.isDirectory && file.canRead()
                    BasicText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clickable(enabled = enabled) {
                                toDir(file)
                            }
                            .wrapContentHeight()
                            .padding(horizontal = 16.dp),
                        text = text,
                    )
                }
            }
        }
    }
}

@Composable
internal fun TreeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val logics = App.logics<TreeLogics>()
        val state = logics.state.collectAsState().value
        LaunchedEffect(Unit) {
            if (state == null) {
                val type = Environment.DIRECTORY_DOCUMENTS
                val current = Environment.getExternalStoragePublicDirectory(type)
                    .parentFile ?: TODO()
                logics.requestState(current = current)
            }
        }
        if (state != null) {
            TreeScreen(
                state = state,
                toParent = {
                    val current = state.parent ?: TODO()
                    logics.requestState(current = current)
                },
                toDir = { file ->
                    logics.requestState(current = file)
                },
            )
        }
    }
}
