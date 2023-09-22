package com.example.bluetooth_jetpackcompose_mvvm

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen.MainScreen
import com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen.ScreenViewModel
import com.example.bluetooth_jetpackcompose_mvvm.ui.theme.Bluetooth_JetPackCompose_MVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN),0)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(viewModel.broadCastReceiver,intentFilter)

        setContent {
            Bluetooth_JetPackCompose_MVVMTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Bluetooth_JetPackCompose_MVVMTheme {
        Greeting("Android")
    }
}