package dev.deviouslypatient.drivershipments.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

// This DefaultDataRepository just holds data in memory, other implementations could be backed by
// databases or other data storage paradigms
class DefaultDataRepository(private val dataService: DataService): DataRepository {
    private var drivers: Array<String> = emptyArray()
    private var shipments: Array<String> = emptyArray()

    override fun retrieveData(): Completable {
        return Completable.create { emitter ->
            dataService
                .retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { data ->
                        Timber.d("Data returned with drivers: ${data.drivers.contentToString()} and shipments: ${data.shipments.contentToString()}")
                        drivers = data.drivers
                        shipments = data.shipments
                        emitter.onComplete()
                    },
                    { e ->
                        Timber.e(e, "Error retrieving data")
                        emitter.onError(e)
                    }
                )
        }
    }

    override fun getDrivers(): Single<Array<String>> {
        return Single.just(drivers)
    }

    override fun getShipments(): Single<Array<String>> {
        return Single.just(shipments)
    }
}