package com.petar.car.sharing.repositories

import com.petar.car.sharing.models.PlaceMarkModel
import com.petar.car.sharing.models.db.DbPlaceMarkModel
import io.reactivex.Observable
import io.realm.Realm

object PlaceMarkRepository {

    fun savePlaceMarksList(list: List<PlaceMarkModel>): Observable<Unit> {
        return Observable.create {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()

            val listToStore = arrayListOf<DbPlaceMarkModel>()
            list.forEach {
                listToStore.add(DbPlaceMarkModel(it))
            }

            realm.delete(DbPlaceMarkModel::class.java)
            realm.copyToRealm(listToStore)

            realm.commitTransaction()
            realm.close()

            it.onComplete()
        }
    }

    fun getPlaceMarksList(): Observable<List<PlaceMarkModel>> {
        return Observable.create {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val query = realm.where(DbPlaceMarkModel::class.java).findAll()
            val storedList = realm.copyFromRealm(query)
            realm.commitTransaction()
            realm.close()

            val listToReturn = arrayListOf<PlaceMarkModel>()

            storedList.forEach {
                listToReturn.add(PlaceMarkModel(it))
            }

            it.onNext(listToReturn)
            it.onComplete()
        }
    }
}