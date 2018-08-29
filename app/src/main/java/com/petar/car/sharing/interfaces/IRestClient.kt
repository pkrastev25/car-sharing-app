package com.petar.car.sharing.interfaces

import com.petar.car.sharing.models.PlaceMarkLocationsModel
import io.reactivex.Observable
import retrofit2.http.GET

interface IRestClient {

    @GET("/wunderbucket/locations.json")
    fun getPlaceMarkLocations(): Observable<PlaceMarkLocationsModel>
}