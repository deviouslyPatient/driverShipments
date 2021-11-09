package dev.deviouslypatient.drivershipments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.deviouslypatient.drivershipments.data.DataService
import dev.deviouslypatient.drivershipments.model.Assignment
import dev.deviouslypatient.drivershipments.model.SuitabilityEngine
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class DriverViewModel(
//    val dataRepository: DataRepository,
    val dataService: DataService,
    val suitabilityEngine: SuitabilityEngine
    ): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val combinations: MutableLiveData<List<Assignment>> by lazy {
        MutableLiveData<List<Assignment>>().also { getDriversAndShipments() }
    }

    fun getCombinations(): LiveData<List<Assignment>> {
        return combinations
    }

    //todo handle loading and error states for the UI
    private fun getDriversAndShipments() {
        compositeDisposable.add(
            dataService
                .retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { result ->
                    compositeDisposable.add(
                        suitabilityEngine
                            .getDriverShipmentAssignments(result.drivers, result.shipments)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ combos ->
                                combinations.postValue(combos)
                            },{
                                Timber.e(it, "Error retrieving most suitable combinations")
                            })
                    )
                }, {
                    Timber.e(it, "Error retrieving data")
                }))
    }

//    fun getDrivers(): LiveData<Array<String>> {
//        return drivers
//    }

//    // todo figure out a better time or way to retrieve data, as that should only need to happen once per day
//    private fun retrieveData() {
//        compositeDisposable.add(
//            dataRepository
//                .retrieveData()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe( {
//                    Timber.d("Data repository retrieved data")
//                    retrieveDrivers()
//                }, {
//                    Timber.e(it, "Error retrieving data")
//                })
//        )
//    }

//    //todo handle loading and error states for the UI
//    private fun retrieveDrivers() {
//        compositeDisposable.add(
//            dataRepository
//                .getDrivers()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe( { result ->
//                    Timber.d("Data Repository returned drivers $result")
//                    drivers.postValue(result)
//                }, {
//                    Timber.e(it, "Error loading drivers from repository")
//                }))
//    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}