package com.robingebert.blokky.navigation

sealed class Screen(val route: String) {
    data object Settings : Screen("settings")
    data object About : Screen("about")
}
