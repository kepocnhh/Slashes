package org.kepocnhh.slashes.module.router

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.BasicSwipeToDismissBox
import org.kepocnhh.slashes.BuildConfig
import org.kepocnhh.slashes.module.tree.TreeScreen

@Deprecated(message = "todo")
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
    val packageName = BuildConfig.APPLICATION_ID
    val appInfo = packageManager.getApplicationInfo(packageName, 0)
    val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val op = AppOpsManager.permissionToOp(Manifest.permission.MANAGE_EXTERNAL_STORAGE) ?: TODO()
    return appOpsManager.unsafeCheckOpNoThrow(op, appInfo.uid, packageName) == AppOpsManager.MODE_ALLOWED
}

@Composable
internal fun OnPermissionNotAllowed() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val scope: BoxWithConstraintsScope = this
        val padding = (scope.maxWidth * (kotlin.math.sqrt(2.0) - 1).toFloat()) / (2 * kotlin.math.sqrt(2.0).toFloat())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BasicText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "This application cannot run without the MANAGE_EXTERNAL_STORAGE permission.",
                style = TextStyle(fontSize = 12.sp)
            )
            BasicText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "As of May 30, 2024, in watches on Wear OS this can be done through adb using the command:",
                style = TextStyle(fontSize = 12.sp)
            )
            BasicText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "adb shell appops set --uid ${BuildConfig.APPLICATION_ID} MANAGE_EXTERNAL_STORAGE allow",
                style = TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            )
        }
    }
}

@Composable
internal fun RouterScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val context = LocalContext.current
        val isStoragePermissionGranted = context.isStoragePermissionGranted()
        if (isStoragePermissionGranted) {
            TreeScreen()
        } else {
            OnPermissionNotAllowed()
        }
    }
}
