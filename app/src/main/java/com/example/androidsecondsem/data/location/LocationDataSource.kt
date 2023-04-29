package com.example.androidsecondsem.data.location

import android.annotation.SuppressLint
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class LocationDataSource(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    private val locationSubject = PublishSubject.create<UserLocationModel>()
    @SuppressLint("MissingPermission")
    fun getLocation(): Flowable<UserLocationModel> = locationSubject.toFlowable(
        BackpressureStrategy.LATEST
    )
        .doOnSubscribe {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                locationSubject.onNext(
                    it.toLocationInfo()
                )
            }.addOnFailureListener {
                UserLocationModel(
                    latitude = 54.5299,
                    longitude = 52.8039,
                )
            }
        }
        .subscribeOn(Schedulers.io())
}
