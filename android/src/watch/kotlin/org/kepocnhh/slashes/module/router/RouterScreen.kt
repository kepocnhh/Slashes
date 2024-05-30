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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.kepocnhh.slashes.BuildConfig

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
    } else {
        Environment.isExternalStorageManager()
    }
}

@Composable
internal fun RouterScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        // todo
    }
}
