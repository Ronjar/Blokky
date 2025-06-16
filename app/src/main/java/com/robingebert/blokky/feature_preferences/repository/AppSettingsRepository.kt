package com.robingebert.blokky.feature_preferences.repository

import android.content.Context
import com.robingebert.blokky.datastore.AppSettings
import com.robingebert.blokky.datastore.DataStoreManager
import com.robingebert.blokky.feature_preferences.repository.models.App
import com.robingebert.blokky.feature_preferences.repository.models.Feature
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val dataStore: DataStoreManager,
    private val context: Context
) {

    val appSettings: Flow<AppSettings>
        get() = dataStore.appSettingsFlow

    suspend fun updateApp(
        appName: String,
        update: (old: App) -> App
    ) {
        // 1. aktuellen Settings-Snapshot holen
        val current = dataStore.appSettingsFlow.first()
        // 2. gezielt die richtige App kopieren und modifizieren
        val modified = when(appName) {
            "Instagram" -> current.copy(instagram = update(current.instagram))
            "YouTube"   -> current.copy(youtube   = update(current.youtube))
            "TikTok"    -> current.copy(tiktok    = update(current.tiktok))
            else        -> current
        }
        // 3. speichern
        dataStore.update(modified)
    }

    suspend fun updateFeature(
        appName: String,
        featureName: String,
        update: (old: Feature) -> Feature
    ) {
        // 1. aktuellen Settings-Snapshot holen
        val current = dataStore.appSettingsFlow.first()
        // 2. Liste der Features der entsprechenden App anpassen
        val newApps = when(appName) {
            "Instagram" -> {
                val updatedFeatures = current.instagram.features.map {
                    if (it.name == featureName) update(it) else it
                }
                current.copy(instagram = current.instagram.copy(features = updatedFeatures))
            }
            "YouTube" -> {
                val updatedFeatures = current.youtube.features.map {
                    if (it.name == featureName) update(it) else it
                }
                current.copy(youtube = current.youtube.copy(features = updatedFeatures))
            }
            else -> current
        }
        // 3. speichern
        dataStore.update(newApps)
    }

    fun applyTemporaryException(
        appName: String,
        featureName: String,
        durationMinutes: Int
    ) {
        FeatureToggleWorker.scheduleTemporaryException(
            context,
            appName,
            featureName,
            durationMinutes
        )
    }
}
