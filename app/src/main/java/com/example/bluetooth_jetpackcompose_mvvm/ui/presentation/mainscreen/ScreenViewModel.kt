package com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.bluetoothmodule.BtActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val btAction : BtActions,
    private val app : Application
):ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state.asStateFlow()


    val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(
            app
        )
    }

    init {
        _state.value.btState = btAction.isBtOn()
        _state.value.pairedDevicesList = btAction.getPaired()
        _state.value.deviceName = btAction.getDeviceName()
    }

    fun startListen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(bundle: Bundle?) {
                Log.d("Voice Input", "In result")
                bundle?.let {
                    val result = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    result?.get(0)?.let {
                            it1 -> Log.d("Voice Input", it1)
                        when(it1.toLowerCase(Locale.getDefault())){
                            "turn on bluetooth" -> {
                                if(!state.value.btState){
                                    btActionChange()
                                } else {

                                }
                            }
                            "turn off bluetooth" -> {
                                if(state.value.btState){
                                    btActionChange()
                                }else{

                                }
                            }

                            "start scan" -> {
                                getAvailableDevices()
                            }

                            "stop scan" -> {
                                cancelDiscovery()
                            }

                            "make discoverable" -> {
                                makeDeviceDiscover()
                            }
                            else ->{
                                Log.d("Voice Input", it1)
                            }
                        }
                    }
                }
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }
        })
        speechRecognizer.startListening(intent)
    }

    fun setBtName(name: String) {
        btAction.setDeviceName(name)
    }

    fun toast(message: String) {
        Toast.makeText(app, message, Toast.LENGTH_LONG).show()
    }

    fun makeDeviceDiscover() {
        btAction.makeDiscover()
    }

    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) {
        device.createBond()
    }

    fun deviceToString(device: BluetoothDevice, code: Int): String {
        if (ActivityCompat.checkSelfPermission(
                app,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Tag","Permission Not given")
        }

        return when(code){
            2 -> device.name.toString()
            1 -> device.bluetoothClass.toString()
            else -> device.toString()
        }
    }

    fun deletePaired(device: BluetoothDevice) {
        device.javaClass.getMethod("removeBond").invoke(device)
        _state.update { screenState ->
            screenState.copy(pairedDevicesList = _state.value.pairedDevicesList - device)
        }
    }

    //This Function call the function in Bluetooth module to turn on Bluetooth
    fun btActionChange() {
        btAction.changeBtAction()
    }

    fun getAvailableDevices() {
        _state.update { screenState ->
            screenState.copy(availableDeviceList = mutableSetOf())
        }
        btAction.discoverDevices()
    }

    fun cancelDiscovery() {
        btAction.cancelDiscovery()
    }

    fun getPairedDevices() {
        if (btAction.isBtOn()) {
            _state.update { screenState ->
                screenState.copy(pairedDevicesList = btAction.getPaired())
            }
        } else {
            _state.update { screenState ->
                screenState.copy(pairedDevicesList = mutableSetOf())
            }
        }
    }

    //BroadCast Receiver
    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    if (btAction.isBtOn()) {
                        _state.update { screenState ->
                            screenState.copy(
                                btState = true,
                                pairedDevicesList = btAction.getPaired()
                            )
                        }
                        getAvailableDevices()
                    } else {
                        cancelDiscovery()
                        _state.update { screenState ->
                            screenState.copy(
                                btState = false,
                                pairedDevicesList = mutableSetOf(),
                                availableDeviceList = mutableSetOf(),
                                discoverState = false
                            )
                        }
                    }
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            app,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (device?.name != null) {
                            if (device !in _state.value.pairedDevicesList) {
                                _state.update { screenState ->
                                    screenState.copy(availableDeviceList = _state.value.availableDeviceList + device)
                                }
                            }
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("Discovery", "Started")
                    if (btAction.isDiscovering()) {
                        _state.update { screenState ->
                            screenState.copy(discoverState = true)
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("Discovery", "Finished")

                    if (!btAction.isDiscovering()) {
                        _state.update { screenState ->
                            screenState.copy(discoverState = false)
                        }
                    }
                }

                BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED -> {
                    _state.update { screenState ->
                        screenState.copy(deviceName = btAction.getDeviceName())
                    }
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if (device != null) {
                        _state.update { screenState ->
                            screenState.copy(
                                pairedDevicesList = btAction.getPaired(),
                                availableDeviceList = _state.value.availableDeviceList - device
                            )
                        }
                    }

                }

            }
        }
    }
}