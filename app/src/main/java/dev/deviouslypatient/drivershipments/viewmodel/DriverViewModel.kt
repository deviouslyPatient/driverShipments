package dev.deviouslypatient.drivershipments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.deviouslypatient.drivershipments.data.DataRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class DriverViewModel(val dataRepository: DataRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val drivers: MutableLiveData<Array<String>> by lazy {
        MutableLiveData<Array<String>>().also { retrieveData() }
    }

    fun getDrivers(): LiveData<Array<String>> {
        return drivers
    }

    // todo figure out a better time or way to retrieve data, as that should only need to happen once per day
    private fun retrieveData() {
        compositeDisposable.add(
            dataRepository
                .retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    Timber.d("Data repository retrieved data")
                    retrieveDrivers()
                }, {
                    Timber.e(it, "Error retrieving data")
                })
        )
    }

    //todo handle loading and error states for the UI
    private fun retrieveDrivers() {
        compositeDisposable.add(
            dataRepository
                .getDrivers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { result ->
                    Timber.d("Data Repository returned drivers $result")
                    drivers.postValue(result)
                }, {
                    Timber.e(it, "Error loading drivers from repository")
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}