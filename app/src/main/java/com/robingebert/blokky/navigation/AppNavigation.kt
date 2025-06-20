package com.robingebert.blokky.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.robingebert.blokky.feature_settings.AboutScreen
import com.robingebert.blokky.feature_preferences.ui.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(modifier = Modifier.padding(4.dp), navController = navController, startDestination = Screen.Settings.route) {
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.About.route) { AboutScreen() }
    }
}