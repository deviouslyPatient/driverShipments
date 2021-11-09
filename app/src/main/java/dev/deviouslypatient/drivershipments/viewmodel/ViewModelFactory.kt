package dev.deviouslypatient.drivershipments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.deviouslypatient.drivershipments.data.DataRepository
import dev.deviouslypatient.drivershipments.data.DataService
import dev.deviouslypatient.drivershipments.data.JsonDataService
import dev.deviouslypatient.drivershipments.model.SuitabilityEngine

class ViewModelFactory(
    val dataRepository: DataRepository,
    val dataService: DataService,
    val suitabilityEngine: SuitabilityEngine
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DriverViewModel::class.java)) {
            val viewModel = DriverViewModel(dataService, suitabilityEngine)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown class Name")
    }
}