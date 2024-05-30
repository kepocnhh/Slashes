package org.kepocnhh.slashes.module.router

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
    val isWatch = packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH)
    if (isWatch) {
        TODO()
    } else {
        val action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        startActivity(Intent(action, uri))
    }
}

private fun Context.isStoragePermissionGranted(): Boolean {
    val isWatch = packageManager.hasSystemFeature(PackageManager.FEATURE_WATCH)
    return if (isWatch) {
        val packageName = BuildConfig.APPLICATION_ID
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val op = AppOpsManager.permissionToOp(Manifest.permission.MANAGE_EXTERNAL_STORAGE) ?: TODO()
        appOpsManager.unsafeCheckOpNoThrow(op, appInfo.uid, packageName) == AppOpsManager.MODE_ALLOWED
//        checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    } else {
        Environment.isExternalStorageManager()
    }
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
        val activity = LocalContext.current as? Activity ?: TODO()
        val lifecycleOwner = LocalLifecycleOwner.current
        val permissionState = remember {
            val value = if (activity.isStoragePermissionGranted()) {
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
                                if (activity.isStoragePermissionGranted()) {
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
                                    activity.requestStoragePermission()
                                },
                            )
                            .wrapContentSize(),
                        text = "request",
                    )
                }
            }
            PermissionState.REQUESTED -> {
                BasicText(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "loading...",
                )
            }
        }
    }
}
