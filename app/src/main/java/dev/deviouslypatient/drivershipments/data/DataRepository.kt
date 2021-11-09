package dev.deviouslypatient.drivershipments.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

// Creating a data repository to separate out how the data is retrieved/loaded and how it is
// provided to the rest of the app. This would create an easy means to set when/how the data is
// updated to limit disk reads/network traffic and hold data readily available in memory
interface DataRepository {
    fun retrieveData(): Completable
    fun getDrivers(): Single<List<String>>
    fun getShipments(): Single<List<String>>
}