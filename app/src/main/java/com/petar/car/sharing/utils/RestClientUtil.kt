package com.petar.car.sharing.utils

import com.petar.car.sharing.interfaces.IRestClient
import com.petar.car.sharing.models.PlaceMarkModel
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClientUtil {

    private const val BASE_URL = "https://s3-us-west-2.amazonaws.com"
    private val REQUEST_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(20)

    private val restClient: IRestClient = initRestClient()

    fun getPlaceMarkLocations(): Observable<List<PlaceMarkModel>> {
        return restClient.getPlaceMarkLocations().switchMap {
            Observable.just(it.placeMarks)
        }
    }

    private fun initRestClient(): IRestClient {
        val httpClient = OkHttpClient.Builder()
                .readTimeout(REQUEST_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(REQUEST_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create()
                ).addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create()
                ).client(httpClient)
                .build()
                .create(IRestClient::class.java)
    }
}