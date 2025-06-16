package com.robingebert.blokky.application

import com.robingebert.blokky.datastore.DataStoreManager
import com.robingebert.blokky.feature_preferences.OverviewViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.androidx.workmanager.dsl.workerOf

object AppModule {
    fun modules() = commonModule+ workerModule + viewModelModule
}

val viewModelModule = module {
    viewModel { OverviewViewModel(dataStoreManager = get()) }
}

val commonModule = module {
    single { DataStoreManager(androidContext()) }
}

val workerModule = module {
    workerOf(::FeatureToggleWorker)
}