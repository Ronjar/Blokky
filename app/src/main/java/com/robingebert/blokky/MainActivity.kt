package com.robingebert.blokky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.robingebert.blokky.ui.theme.BlokkyTheme
import com.robingebert.blokky.feature_app.navigation.AppNavigation
import com.robingebert.blokky.feature_app.navigation.Screen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            BlokkyTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    topBar = {
                        TopAppBar(
                            title = { Text(navController.currentScreenTitle()) },
                            actions = {
                                if (navController.currentScreenTitle() != "About") {
                                    IconButton(onClick = {
                                        navController.navigate(Screen.About.route)
                                    }) {
                                        Icon(
                                            Icons.Rounded.Info,
                                            contentDescription = "About"
                                        )
                                    }
                                }
                            },
                            navigationIcon = {
                                if (navController.currentScreenTitle() != "Blokky") {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            Icons.AutoMirrored.Rounded.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            }
                        )
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            AppNavigation(navController = navController)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NavController.currentScreenTitle(): String {
    val currentRoute = currentBackStackEntryAsState().value?.destination?.route
    return when (currentRoute) {
        Screen.About.route -> "About"
        else -> "Blokky"
    }
}