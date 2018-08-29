package com.petar.car.sharing.models.db

import com.petar.car.sharing.extensions.toRealmList
import com.petar.car.sharing.models.PlaceMarkModel
import io.realm.RealmList
import io.realm.RealmObject

open class DbPlaceMarkModel(
        var address: String = "",
        var coordinates: RealmList<Double> = RealmList(),
        var engineType: String = "",
        var exterior: String = "",
        var fuel: Int = 0,
        var interior: String = "",
        var name: String = "",
        var vin: String = ""
) : RealmObject() {
    constructor(placeMarkModel: PlaceMarkModel) : this(
            placeMarkModel.address,
            placeMarkModel.coordinates.toRealmList(),
            placeMarkModel.engineType,
            placeMarkModel.exterior,
            placeMarkModel.fuel,
            placeMarkModel.interior,
            placeMarkModel.name,
            placeMarkModel.vin
    )
}