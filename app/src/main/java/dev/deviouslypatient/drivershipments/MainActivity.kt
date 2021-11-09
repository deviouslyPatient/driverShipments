package dev.deviouslypatient.drivershipments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.deviouslypatient.drivershipments.data.DataRepository
import dev.deviouslypatient.drivershipments.data.DataService
import dev.deviouslypatient.drivershipments.data.DefaultDataRepository
import dev.deviouslypatient.drivershipments.data.JsonDataService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        // todo change this to use an injected DataRepository
        // This will load the data every time the Main Activity is started
        val dataService: DataService = JsonDataService(this.applicationContext)
        val dataRepository: DataRepository = DefaultDataRepository(dataService)
        dataRepository
            .retrieveData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.d("Data retrieved")
                },
                { e ->
                    Timber.e(e, "Error retrieving data")
                }
            )
    }
}