package dev.deviouslypatient.drivershipments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.deviouslypatient.drivershipments.data.DataService
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
        // todo change this to be an injected DataService
        val dataService: DataService = JsonDataService(this.applicationContext)
        dataService
            .retrieveData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    Timber.d("Data returned with drivers: ${data.drivers.contentToString()} and shipments: ${data.shipments.contentToString()}")
                },
                { e ->
                    Timber.e(e, "Error retrieving data")
                }
            )
    }
}