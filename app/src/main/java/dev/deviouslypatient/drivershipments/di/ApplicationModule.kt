package dev.deviouslypatient.drivershipments.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.deviouslypatient.drivershipments.data.DataRepository
import dev.deviouslypatient.drivershipments.data.DataService
import dev.deviouslypatient.drivershipments.data.DefaultDataRepository
import dev.deviouslypatient.drivershipments.data.JsonDataService
import dev.deviouslypatient.drivershipments.data.DefaultSuitabilityEngine
import dev.deviouslypatient.drivershipments.data.SuitabilityEngine
import dev.deviouslypatient.drivershipments.viewmodel.ViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindsDataService (jsonDataService: JsonDataService): DataService

    @Binds
    @Singleton
    abstract fun bindsSuitabilityEngine(defaultSuitabilityEngine: DefaultSuitabilityEngine): SuitabilityEngine

    @Binds
    @Singleton
    abstract fun bindsDataRepository(defaultDataRepository: DefaultDataRepository): DataRepository

    @Binds
    @Singleton
    abstract fun bindsViewModelProviderFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}
