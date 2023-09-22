package com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen

import android.bluetooth.BluetoothDevice

data class ScreenState(
    var btState: Boolean = false,
    var pairedDevicesList: Set<BluetoothDevice> = mutableSetOf()
)
