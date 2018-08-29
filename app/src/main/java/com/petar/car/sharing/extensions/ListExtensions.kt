package com.petar.car.sharing.extensions

import io.realm.RealmList

fun <TModel> List<TModel>.toRealmList(): RealmList<TModel> {
    val realmList = RealmList<TModel>()

    this.forEach {
        realmList.add(it)
    }

    return realmList
}
