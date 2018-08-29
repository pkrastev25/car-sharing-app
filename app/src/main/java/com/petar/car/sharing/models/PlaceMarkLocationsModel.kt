package com.petar.car.sharing.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList

data class PlaceMarkLocationsModel(
        @SerializedName("placemarks")
        val placeMarks: RealmList<PlaceMarkModel> = RealmList()
)