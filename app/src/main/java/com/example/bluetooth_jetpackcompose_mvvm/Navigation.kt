package com.example.bluetooth_jetpackcompose_mvvm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen.MainScreen
import com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen.NameScreen
import com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen.ScreenViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation(viewModel: ScreenViewModel) {
    val navController = rememberNavController()

    val uiState by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = "mainScreen" ){
        composable(route = "mainScreen") {
            MainScreen(viewModel,navController = navController,uiState)
        }

        composable(route = "nameScreen"){
            NameScreen(viewModel,navController = navController,uiState)
        }
    }
}
