package dev.deviouslypatient.drivershipments.data

import dev.deviouslypatient.drivershipments.model.Assignment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

// This DefaultDataRepository just holds data in memory, other implementations could be backed by
// databases or other data storage paradigms
class DefaultDataRepository @Inject constructor(
    private val dataService: DataService,
    private val suitabilityEngine: SuitabilityEngine
    ): DataRepository {

    private var drivers: List<String> = listOf()
    private var shipments: List<String> = listOf()
    private var assignments: List<Assignment> = listOf()

    override fun retrieveData(): Completable {
        return Completable.create { emitter ->
            dataService
                .retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { data ->
                        Timber.d("Data returned with drivers: ${data.drivers} and shipments: ${data.shipments}")
                        drivers = data.drivers
                        shipments = data.shipments
                        assignments = suitabilityEngine.calculateDriverShipmentAssignments(drivers, shipments)
                        emitter.onComplete()
                    },
                    { e ->
                        Timber.e(e, "Error retrieving data")
                        emitter.onError(e)
                    }
                )
        }
    }

    override fun getDrivers(): Single<List<String>> {
        return Single.just(drivers)
    }

    override fun getShipments(): Single<List<String>> {
        return Single.just(shipments)
    }

    override fun getDriverShipmentAssignments(): Single<List<Assignment>> {
        return Single.just(assignments)
    }
}