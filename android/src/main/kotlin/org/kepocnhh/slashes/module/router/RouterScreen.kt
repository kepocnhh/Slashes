package org.kepocnhh.slashes.module.router

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import org.kepocnhh.slashes.BuildConfig
import org.kepocnhh.slashes.module.tree.TreeScreen

internal object RouterScreen {
    enum class PermissionState {
        REQUESTED,
        GRANTED,
    }
}

@Composable
internal fun RouterScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val activity = LocalContext.current as? Activity
        val lifecycleOwner = LocalLifecycleOwner.current
        val permissionState = remember { mutableStateOf<RouterScreen.PermissionState?>(null) }
        LaunchedEffect(Unit) {
            lifecycleOwner.lifecycle.currentStateFlow.collect {
                when (it) {
                    Lifecycle.State.RESUMED -> {
                        when (permissionState.value) {
                            RouterScreen.PermissionState.GRANTED -> {
                                // noop
                            }
                            RouterScreen.PermissionState.REQUESTED -> {
                                val granted = Environment.isExternalStorageManager()
                                if (granted) {
                                    permissionState.value = RouterScreen.PermissionState.GRANTED
                                } else {
                                    onBack()
                                }
                            }
                            null -> {
                                val granted = Environment.isExternalStorageManager()
                                if (granted) {
                                    permissionState.value = RouterScreen.PermissionState.GRANTED
                                } else {
                                    if (activity == null) TODO()
                                    val action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                                    val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                                    activity.startActivity(Intent(action, uri))
                                    permissionState.value = RouterScreen.PermissionState.REQUESTED
                                }
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
            RouterScreen.PermissionState.GRANTED -> {
                TreeScreen()
            }
            else -> {
                // noop
            }
        }
    }
}
