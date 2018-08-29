package com.petar.car.sharing.general

import android.app.Application
import io.realm.Realm

class CarSharingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}