package com.robingebert.blokky.datastore

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.dataStore
import com.robingebert.blokky.models.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn

val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)

class DataStoreManager(
    private val context: Context
) {
    val appSettings = context.dataStore.data

    suspend fun updateAppSettings(appSettings: AppSettings) {
        context.dataStore.updateData {
            appSettings
        }
    }

    suspend fun updateInstagram(app: App){
        context.dataStore.updateData {
            appSettings.last().copy(instagram = app)
        }
    }
    suspend fun updateTikTok(app: App){
        context.dataStore.updateData {
            appSettings.last().copy(tiktok = app)
        }
    }
    suspend fun updateYoutube(app: App){
        context.dataStore.updateData {
            appSettings.last().copy(youtube = app)
        }
    }
}