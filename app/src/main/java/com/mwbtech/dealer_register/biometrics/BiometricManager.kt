package com.mwbtech.dealer_register.biometrics

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mwbtech.dealer_register.R

class BiometricManager(private val context: Activity) {

    fun checkFingerPrint(listener: BioMetricListener) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> listener.onCheck(BiometricStatus.BIOMETRIC_AVAILABLE)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> listener.onCheck(BiometricStatus.BIOMETRIC_NOT_FOUND)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> listener.onCheck(BiometricStatus.BIOMETRIC_NOT_ENABLE)
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> listener.onCheck(BiometricStatus.BIOMETRIC_UNKNOWN)
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> listener.onCheck(BiometricStatus.BIOMETRIC_NOT_AVAILABLE)
        }
    }

    fun enrollNewFinger() {
        val enrollIntent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> Intent(Settings.ACTION_BIOMETRIC_ENROLL)
            else -> Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        context.startActivity(enrollIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun verifyFinerPrint(listener: BioMetricListener) {
        val authenticationCallBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onCheck(BiometricStatus.BIOMETRIC_SUCCESS)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onCheck(BiometricStatus.BIOMETRIC_ERROR)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                listener.onCheck(BiometricStatus.BIOMETRIC_CANCELLED)
            }
        }
        val biometricPrompt = BiometricPrompt((context as FragmentActivity), ContextCompat.getMainExecutor(context), authenticationCallBack)
        val promptInfo = PromptInfo.Builder().setTitle(context.getString(R.string.title_fingerprint))
                .setSubtitle(context.getString(R.string.desc_fingerprint))
                .setNegativeButtonText(context.getString(android.R.string.cancel)).build()
        biometricPrompt.authenticate(promptInfo)
    }
}

fun interface BioMetricListener {
    fun onCheck(status: String)
}

class BiometricStatus {
    companion object{
        const val BIOMETRIC_AVAILABLE = "BIOMETRIC_AVAILABLE"
        const val BIOMETRIC_SUCCESS = "BIOMETRIC_SUCCESS"
        const val BIOMETRIC_NOT_FOUND = "BIOMETRIC_NOT_FOUND"
        const val BIOMETRIC_NOT_AVAILABLE = "BIOMETRIC_NOT_AVAILABLE"
        const val BIOMETRIC_NOT_ENABLE = "BIOMETRIC_NOT_ENABLE"
        const val BIOMETRIC_ERROR = "BIOMETRIC_ERROR"
        const val BIOMETRIC_CANCELLED = "BIOMETRIC_CANCELLED"
        const val BIOMETRIC_UNKNOWN = "BIOMETRIC_UNKNOWN"
    }
}