package dev.deviouslypatient.drivershipments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.deviouslypatient.drivershipments.data.DataRepository

class ViewModelFactory(val dataRepository: DataRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DriverViewModel::class.java)) {
            val viewModel = DriverViewModel(dataRepository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown class Name")
    }
}