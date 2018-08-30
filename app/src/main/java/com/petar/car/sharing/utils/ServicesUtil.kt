package com.petar.car.sharing.utils

import android.content.Context
import android.location.LocationManager

object ServicesUtil {

    fun isGpsProviderEnabled(context: Context): Boolean {
        return try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }
}