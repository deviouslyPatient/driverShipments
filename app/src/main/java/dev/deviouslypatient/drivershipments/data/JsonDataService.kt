package dev.deviouslypatient.drivershipments.data

import android.content.Context
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.nio.charset.Charset

class JsonDataService(val context: Context): DataService {
    override fun retrieveData(): Single<DataResponse> {
        return Single.create { emitter ->
            try {
                val data = Gson().fromJson(
                    context.assets.open("data.json").reader(Charset.defaultCharset()),
                    DataResponse::class.java
                )
                emitter.onSuccess(data)
            } catch (e: Exception) {
                Timber.e(e, "Error reading from json file.")
                emitter.onError(e)
            }
        }
    }
}