package com.example.Bluetooth2nd

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.Bluetooth2nd.ui.presentation.mainscreen.MainScreen
import com.example.Bluetooth2nd.ui.presentation.mainscreen.NameScreen
import com.example.Bluetooth2nd.ui.presentation.mainscreen.ScreenViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation(viewModel: ScreenViewModel) {
    val navController = rememberNavController()

    val uiState by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = Screen.MainScreen.route ){
        composable(route = Screen.MainScreen.route) {
            MainScreen(viewModel,navController = navController,uiState)
        }

        composable(route = Screen.NameScreen.route){
            NameScreen(viewModel,navController = navController,uiState)
        }
    }
}
