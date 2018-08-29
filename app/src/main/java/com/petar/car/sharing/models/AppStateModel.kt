package com.petar.car.sharing.models

sealed class AppStateModel {

    data class DataState<out TModel>(
            val data: TModel
    ) : AppStateModel()

    object LoadingState : AppStateModel()

    data class ErrorState(
            val error: Throwable
    ) : AppStateModel()
}