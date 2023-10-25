package com.mwbtech.dealer_register.Utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.mwbtech.dealer_register.R

fun Context.openCustomTab(url: String?) {
    if (url == null) return
    val tabIntentBuilder = CustomTabsIntent.Builder()
    val defaultColors = CustomTabColorSchemeParams.Builder().setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary)).build()
    tabIntentBuilder.apply {
        setDefaultColorSchemeParams(defaultColors)
    }.build().launchUrl(this, Uri.parse(url))
}

fun Context.checkCameraPermission(status: (status: Boolean) -> Unit) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        status(true)
    }else{
        CustomToast.showToast(this, "Camera permission not provided")
    }
}