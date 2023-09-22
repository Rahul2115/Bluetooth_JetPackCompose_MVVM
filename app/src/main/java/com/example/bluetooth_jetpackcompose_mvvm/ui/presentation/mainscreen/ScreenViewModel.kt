package com.example.bluetooth_jetpackcompose_mvvm.ui.presentation.mainscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.bluetoothmodule.BtActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScreenViewModel @Inject constructor(
    private val btAction : BtActions
):ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    init{
        _state.value.btState = btAction.isBtOn()
    }

    //This Function call the function in Bluetooth module to turn on Bluetooth
    fun switchBtState(){
            btAction.btOn()
    }


    //BroadCast Receiver which receives bluetooth Action Changed
    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if(btAction.isBtOn())
            {
                _state.update { screenState ->
                    screenState.copy(btState = true)
                }
            }else{
                _state.update { screenState ->
                    screenState.copy(btState = false)
                }
            }
        }
    }

}