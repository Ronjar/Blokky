package com.robingebert.blokky.datastore

import android.content.Context
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.first

val Context.appSettingsStore by dataStore("app_settings.json", AppSettingsSerializer)
val Context.featureEnabledStore by dataStore("feature_enabled.json", FeatureEnabledSerializer)


class DataStoreManager(private val context: Context) {

    val appSettingsFlow = context.appSettingsStore.data
    val featureEnabledFlow = context.featureEnabledStore.data

    suspend fun updateAppSettings(settings: AppSettings) {
        context.appSettingsStore.updateData { settings }
    }

    suspend fun updateFeatureStatus(appName: String, featureName: String?, enabled: Boolean) {
        context.featureEnabledStore.updateData { prefs ->
            val updatedStatuses = prefs.statuses.filterNot {
                it.appName == appName && it.featureName == featureName
            } + FeatureEnabledStatus(appName, featureName, enabled)

            prefs.copy(statuses = updatedStatuses)
        }
    }

    suspend fun getFeatureEnabledStatus(appName: String, featureName: String?): Boolean {
        return featureEnabledFlow.first().statuses
            .firstOrNull { it.appName == appName && it.featureName == featureName }
            ?.enabled ?: false
    }
}
