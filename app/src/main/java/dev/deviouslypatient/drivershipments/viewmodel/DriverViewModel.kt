package dev.deviouslypatient.drivershipments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deviouslypatient.drivershipments.data.DataRepository
import dev.deviouslypatient.drivershipments.model.Assignment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

// I'm not sure how much sense it makes to have a data repository and a viewModel in an app with
// only one view, especially if the model doesn't ever get updated by interactions with the app
@HiltViewModel
class DriverViewModel @Inject constructor(private val dataRepository: DataRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val combinations: MutableLiveData<List<Assignment>> by lazy {
        // todo figure out a better time or way to retrieve data, as that should only need to happen once per day
        MutableLiveData<List<Assignment>>().also { retrieveData() }
    }

    fun getCombinations(): LiveData<List<Assignment>> {
        return combinations
    }

    // todo handle posting updates for loading and error states
    private fun retrieveData() {
        compositeDisposable.add(
            dataRepository
                .retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    Timber.d("Data repository retrieved data")
                    retrieveDriverShipmentAssignments()
                }, {
                    Timber.e(it, "Error retrieving data")
                })
        )
    }

    private fun retrieveDriverShipmentAssignments() {
        compositeDisposable.add(
            dataRepository
                .getDriverShipmentAssignments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { result ->
                    Timber.d("Data Repository returned drivers $result")
                    combinations.postValue(result)
                }, {
                    Timber.e(it, "Error loading drivers from repository")
                }))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}