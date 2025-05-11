package com.robingebert.blokky.feature_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robingebert.blokky.datastore.AppSettings
import com.robingebert.blokky.datastore.DataStoreManager
import com.robingebert.blokky.models.App
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    val appSettings: StateFlow<AppSettings> =
        dataStoreManager.appSettings
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AppSettings()
            )

    fun updateInstagram(app: App) {
        viewModelScope.launch {
            dataStoreManager.update(appSettings.value.copy(instagram = app))
        }
    }
    fun updateTikTok(app: App) {
        viewModelScope.launch {
            dataStoreManager.update(appSettings.value.copy(tiktok = app))
        }
    }
    fun updateYoutube(app: App) {
        viewModelScope.launch {
            dataStoreManager.update(appSettings.value.copy(youtube = app))
        }
    }
}