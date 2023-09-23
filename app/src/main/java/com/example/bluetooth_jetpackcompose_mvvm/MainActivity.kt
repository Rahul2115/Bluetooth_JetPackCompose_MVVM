package com.example.bluetooth_jetpackcompose_mvvm

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen.ScreenViewModel
import com.example.bluetooth_jetpackcompose_mvvm.ui.theme.Bluetooth_JetPackCompose_MVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ScreenViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN),0)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(viewModel.broadCastReceiver,intentFilter)

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(viewModel.btDeviceReceiver, filter)

        val nameFilter = IntentFilter()
        nameFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)
        registerReceiver(viewModel.nameReceiver,nameFilter)



        setContent {
            Bluetooth_JetPackCompose_MVVMTheme {
                Navigation(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPairedDevices()
    }
}