package com.example.Bluetooth2nd


sealed class Screen(val route:String){
    object MainScreen: Screen("mainScreen")
    object NameScreen: Screen("nameScreen")
}
