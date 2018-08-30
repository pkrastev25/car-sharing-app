package com.petar.car.sharing.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

object PermissionsUtil {

    fun areWriteExternalStoragePermissionsGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun areAccessFineLocationPermissionsGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun areAllPermissionsGranted(context: Context): Boolean {
        return areWriteExternalStoragePermissionsGranted(context) &&
                areAccessFineLocationPermissionsGranted(context)
    }
}