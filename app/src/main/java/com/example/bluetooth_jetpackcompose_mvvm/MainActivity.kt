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
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.RECORD_AUDIO),0)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED)
        registerReceiver(viewModel.broadCastReceiver,intentFilter)

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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(viewModel.broadCastReceiver)
    }
}