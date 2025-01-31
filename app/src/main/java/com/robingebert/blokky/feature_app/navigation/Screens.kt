package com.robingebert.blokky.feature_app.navigation

sealed class Screen(val route: String) {
    data object Settings : Screen("settings")
    data object About : Screen("about")
}
