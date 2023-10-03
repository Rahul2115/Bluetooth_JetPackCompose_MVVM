package com.example.Bluetooth2nd.ui.presentation.mainscreen

import android.bluetooth.BluetoothDevice

data class ScreenState(
    var btState: Boolean = false,
    var discoverState: Boolean = false,
    var pairedDevicesList: Set<BluetoothDevice> = mutableSetOf(),
    var availableDeviceList: Set<BluetoothDevice> = mutableSetOf(),
    var deviceName: String = "",
)
