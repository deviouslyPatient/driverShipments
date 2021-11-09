package dev.deviouslypatient.drivershipments.data

import io.reactivex.rxjava3.core.Single

interface DataService {
    fun retrieveData(): Single<DataResponse>
}