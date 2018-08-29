package com.petar.car.sharing.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.petar.car.sharing.models.AppStateModel
import com.petar.car.sharing.models.PlaceMarkModel
import com.petar.car.sharing.repositories.PlaceMarkRepository
import com.petar.car.sharing.utils.RestClientUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm

class PlaceMarksViewModel : ViewModel() {

    private val placeMarksLiveData: MutableLiveData<AppStateModel> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
        Realm.getDefaultInstance().close()
    }

    fun getPlaceMarksLiveData(): LiveData<AppStateModel> {
        return placeMarksLiveData
    }

    fun getCachedPlaceMarks() {
        if (placeMarksLiveData.value is AppStateModel.DataState<*>) {
            return
        }

        compositeDisposable.add(
                PlaceMarkRepository.getPlaceMarksList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .filter {
                            it.isNotEmpty()
                        }.switchIfEmpty {
                            loadPlaceMarks()
                        }.doOnSubscribe {
                            placeMarksLiveData.value = AppStateModel.LoadingState
                        }.subscribe({ response ->
                            placeMarksLiveData.value = AppStateModel.DataState(response)
                        }, { error ->
                            placeMarksLiveData.value = AppStateModel.ErrorState(error)
                        })
        )
    }

    fun loadPlaceMarks() {
        compositeDisposable.add(
                RestClientUtil.getPlaceMarkLocations()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe {
                            placeMarksLiveData.value = AppStateModel.LoadingState
                        }.subscribe({ response ->
                            placeMarksLiveData.value = AppStateModel.DataState(response)
                            cachePlaceMarks(response)
                        }, { error ->
                            placeMarksLiveData.value = AppStateModel.ErrorState(error)
                        })
        )
    }

    private fun cachePlaceMarks(list: List<PlaceMarkModel>) {
        compositeDisposable.add(
                PlaceMarkRepository.savePlaceMarksList(list)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnError {
                            placeMarksLiveData.value = AppStateModel.ErrorState(it)
                        }.subscribe()

        )
    }
}