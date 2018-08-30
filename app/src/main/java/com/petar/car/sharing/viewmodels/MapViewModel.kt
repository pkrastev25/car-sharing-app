package com.petar.car.sharing.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val lastMapPositionLiveData = MutableLiveData<List<Double>>()

    fun getLastMapPositionLiveData(): LiveData<List<Double>> {
        return lastMapPositionLiveData
    }

    fun saveMapPosition(latitude: Double, longitude: Double, zoomLevel: Double) {
        lastMapPositionLiveData.value = listOf(latitude, longitude, zoomLevel)
    }
}