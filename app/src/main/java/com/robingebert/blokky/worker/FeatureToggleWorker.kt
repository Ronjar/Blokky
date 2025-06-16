import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.robingebert.blokky.datastore.DataStoreManager
import java.util.concurrent.TimeUnit

class FeatureToggleWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val dataStoreManager = DataStoreManager(context)

    override suspend fun doWork(): Result {
        val appName = inputData.getString("appName") ?: return Result.failure()
        val featureName = inputData.getString("featureName") // null bedeutet App-Level

        val enable = inputData.getBoolean("enable", true)

        dataStoreManager.updateFeatureStatus(appName, featureName, enable)

        return Result.success()
    }

    companion object {
        fun schedule(
            context: Context,
            appName: String,
            featureName: String?,
            enableTimeMinutes: Int,
            disableTimeMinutes: Int
        ) {
            val workManager = WorkManager.getInstance(context)

            val enableRequest = PeriodicWorkRequestBuilder<FeatureToggleWorker>(
                1, TimeUnit.DAYS
            )
                .setInitialDelay(enableTimeMinutes.toLong(), TimeUnit.MINUTES)
                .setInputData(
                    workDataOf(
                        "appName" to appName,
                        "featureName" to featureName,
                        "enable" to true
                    )
                )
                .build()

            val disableRequest = PeriodicWorkRequestBuilder<FeatureToggleWorker>(
                1, TimeUnit.DAYS
            )
                .setInitialDelay(disableTimeMinutes.toLong(), TimeUnit.MINUTES)
                .setInputData(
                    workDataOf(
                        "appName" to appName,
                        "featureName" to featureName,
                        "enable" to false
                    )
                )
                .build()

            workManager.enqueueUniquePeriodicWork(
                "${appName}_${featureName}_enable",
                ExistingPeriodicWorkPolicy.REPLACE,
                enableRequest
            )

            workManager.enqueueUniquePeriodicWork(
                "${appName}_${featureName}_disable",
                ExistingPeriodicWorkPolicy.REPLACE,
                disableRequest
            )
        }

        fun scheduleTemporaryException(
            context: Context,
            appName: String,
            featureName: String?,
            durationMinutes: Int
        ) {
            val workManager = WorkManager.getInstance(context)

            // Sofort deaktivieren
            val disableRequest = OneTimeWorkRequestBuilder<FeatureToggleWorker>()
                .setInputData(
                    workDataOf(
                        "appName" to appName,
                        "featureName" to featureName,
                        "enable" to false
                    )
                ).build()

            // Nach durationMinutes wieder aktivieren
            val enableRequest = OneTimeWorkRequestBuilder<FeatureToggleWorker>()
                .setInitialDelay(durationMinutes.toLong(), TimeUnit.MINUTES)
                .setInputData(
                    workDataOf(
                        "appName" to appName,
                        "featureName" to featureName,
                        "enable" to true
                    )
                ).build()

            workManager.beginUniqueWork(
                "${appName}_${featureName}_temporary_exception",
                ExistingWorkPolicy.REPLACE,
                disableRequest
            ).then(enableRequest).enqueue()
        }
    }
}
