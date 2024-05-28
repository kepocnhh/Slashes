package org.kepocnhh.slashes.module.router

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import org.kepocnhh.slashes.BuildConfig
import org.kepocnhh.slashes.module.tree.TreeScreen

private fun Activity.requestStoragePermission() {
    val action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
    val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
    startActivity(Intent(action, uri))
}

private enum class PermissionState {
    GRANTED,
    REQUESTED,
    REJECTED,
}

@Composable
internal fun RouterScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val activity = LocalContext.current as? Activity
        val lifecycleOwner = LocalLifecycleOwner.current
        val permissionState = remember {
            val value = if (Environment.isExternalStorageManager()) {
                PermissionState.GRANTED
            } else {
                PermissionState.REJECTED
            }
            mutableStateOf<PermissionState>(value)
        }
        LaunchedEffect(Unit) {
            lifecycleOwner.lifecycle.currentStateFlow.collect {
                when (it) {
                    Lifecycle.State.RESUMED -> {
                        when (permissionState.value) {
                            PermissionState.GRANTED -> {
                                // noop
                            }
                            PermissionState.REQUESTED -> {
                                if (Environment.isExternalStorageManager()) {
                                    permissionState.value = PermissionState.GRANTED
                                } else {
                                    permissionState.value = PermissionState.REJECTED
                                }
                            }
                            PermissionState.REJECTED -> {
                                // noop
                            }
                        }
                    }
                    else -> {
                        // noop
                    }
                }
            }
        }
        when (permissionState.value) {
            PermissionState.GRANTED -> {
                TreeScreen()
            }
            PermissionState.REJECTED -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                ) {
                    BasicText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .wrapContentSize(),
                        text = "no permission",
                    )
                    BasicText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clickable(
                                enabled = permissionState.value == PermissionState.REJECTED,
                                onClick = {
                                    permissionState.value = PermissionState.REQUESTED
                                    if (activity == null) TODO()
                                    activity.requestStoragePermission()
                                },
                            )
                            .wrapContentSize(),
                        text = "request",
                    )
                }
            }
            PermissionState.REQUESTED, null -> {
                BasicText(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "loading...",
                )
            }
        }
    }
}
