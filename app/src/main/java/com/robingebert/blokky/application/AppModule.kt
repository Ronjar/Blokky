package com.robingebert.blokky.application

import com.robingebert.blokky.datastore.DataStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    fun modules() = commonModule + viewModelModule
}

val viewModelModule = module {
    viewModel { LauncherViewModel(dataStoreManager = get(), snackbarController = get()) }
}

val commonModule = module {
    single { DataStoreManager(androidContext()) }
}