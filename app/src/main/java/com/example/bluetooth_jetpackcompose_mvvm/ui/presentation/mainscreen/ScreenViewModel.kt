package com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.bluetoothmodule.BtActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val btAction : BtActions,
    private val app : Application
):ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    init{
        _state.value.btState = btAction.isBtOn()
        _state.value.pairedDevicesList = btAction.getPaired()
    }

    fun deviceToString(device: BluetoothDevice,code: Int) : String{
        if (ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }

        if(code == 2)
            return device.name.toString()

        else if(code == 1)
            return device.bluetoothClass.toString()

        else
            return device.toString()
    }

    fun deletePaired(device: BluetoothDevice){
        device.javaClass.getMethod("removeBond").invoke(device)
        _state.update { screenState ->
            screenState.copy(pairedDevicesList = _state.value.pairedDevicesList - device )
        }
    }

    //This Function call the function in Bluetooth module to turn on Bluetooth
    fun btActionChange(){
            btAction.changeBtAction()
    }

    fun getAvailableDevices() {
        btAction.discoverDevices()
    }

    fun getPairedDevices(){
        if(btAction.isBtOn())
        {
            _state.update { screenState ->
                screenState.copy(pairedDevicesList = btAction.getPaired() )
            }
        }else{
            _state.update { screenState ->
                screenState.copy(pairedDevicesList = mutableSetOf())
            }
        }
    }

    //BroadCast Receiver which receives bluetooth Action Changed
    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if(btAction.isBtOn())
            {
                _state.update { screenState ->
                    screenState.copy(btState = true, pairedDevicesList = btAction.getPaired() )
                }
            }else{
                _state.update { screenState ->
                    screenState.copy(btState = false, pairedDevicesList = mutableSetOf())
                }
            }
        }
    }

    val btDeviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val action: String? = intent?.action

            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            app,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (device?.name != null) {
                            if(device !in _state.value.pairedDevicesList)
                            {
                                _state.value.availableDeviceList = _state.value.availableDeviceList + device
                            }
                        }
                    }
                }
            }
        }
    }

}