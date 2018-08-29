package com.petar.car.sharing.models

import com.google.gson.annotations.SerializedName
import com.petar.car.sharing.models.db.DbPlaceMarkModel

data class PlaceMarkModel(
        @SerializedName("address")
        val address: String = "",
        @SerializedName("coordinates")
        val coordinates: List<Double> = arrayListOf(),
        @SerializedName("engineType")
        val engineType: String = "",
        @SerializedName("exterior")
        val exterior: String = "",
        @SerializedName("fuel")
        val fuel: Int = 0,
        @SerializedName("interior")
        val interior: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("vin")
        val vin: String = ""
) {
    constructor(dbPlaceMarkModel: DbPlaceMarkModel) : this(
            dbPlaceMarkModel.address,
            dbPlaceMarkModel.coordinates,
            dbPlaceMarkModel.engineType,
            dbPlaceMarkModel.exterior,
            dbPlaceMarkModel.fuel,
            dbPlaceMarkModel.interior,
            dbPlaceMarkModel.name,
            dbPlaceMarkModel.vin
    )
}